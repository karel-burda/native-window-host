package com.github.karelburda.nativewindowembedder;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import java.awt.Canvas;
import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.karelburda.nativewindowembedder.exceptions.InvalidNativeHandle;
import com.github.karelburda.nativewindowembedder.exceptions.NativeError;

/**
 * Component displays native window handle within itself ("embeddeding").
 * The NativeWindowEmbedder can display most of native windows (currentl win32) windows using Canvas component from SWT.
 * @see demo/Main.java
 */
public class NativeWindowEmbedder extends Canvas {
    private HWND embedded = null;
    private HWND previousParentOfEmbedded = null;
    private HWND embedder = null;
    private AtomicBoolean isHostingNativeWindow = new AtomicBoolean(false);

    /**
     * @param nativeWindowClassName string with win32 window class name.
     */
    public NativeWindowEmbedder(final String nativeWindowClassName) throws InvalidNativeHandle {
        this(User32.INSTANCE.FindWindow(null, nativeWindowClassName));
    }

    /**
     * @param nativeWindowHandle handle (HWND) of a window to be embedded.
     */
    public NativeWindowEmbedder(final HWND nativeWindowHandle) throws InvalidNativeHandle {
        embedded = nativeWindowHandle;

        checkNativeHandleOfEmbedded();
    }

    private NativeWindowEmbedder() {
    }

    /**
     * Releases the underlying handle by setting its parent to the previous handle (previous parent).
     */
    public void release() throws NativeError {
        if (isHostingNativeWindow.get()) {
            if (User32.INSTANCE.SetParent(embedded, previousParentOfEmbedded) == null) {
                NativeError.throwWhenCodeIsNonZero("Failure when trying to call SetParent(...)", Kernel32.INSTANCE.GetLastError());
            }

            isHostingNativeWindow.set(false);
        }
    }

    @Override
    public void paint(final Graphics graphics) throws NativeError {
        super.paint(graphics);

        embedNativeWindow();

        if (!User32.INSTANCE.MoveWindow(embedded, 0, 0, getWidth(), getHeight(), true)) {
            NativeError.throwWhenCodeIsNonZero ("Failure when trying to call MoveWindow(...)",Kernel32.INSTANCE.GetLastError());
        }
    }

    private void embedNativeWindow() throws NativeError {
        if (!isHostingNativeWindow.get()) {
            loadNativeHandleOfEmbedder();

            if (embedded != null && embedder != null) {
                // setting WS_CHILD to the embedded window proved useless during empiric tests

                previousParentOfEmbedded = User32.INSTANCE.GetAncestor(embedded, User32.GA_PARENT);

                if (User32.INSTANCE.SetParent(embedded, embedder) == null) {
                    NativeError.throwWhenCodeIsNonZero("Failure when trying to call SetParent(...)", Kernel32.INSTANCE.GetLastError());
                }

                if (!User32.INSTANCE.ShowWindow(embedder, User32.INSTANCE.SW_SHOW)) {
                    NativeError.throwWhenCodeIsNonZero("Failure when trying to call ShowWindow(...)", Kernel32.INSTANCE.GetLastError());
                }

                isHostingNativeWindow.set(true);

                repaint();
            }
        }
    }

    private void checkNativeHandleOfEmbedded() throws NativeError, InvalidNativeHandle {
        if (embedded == null) {
            throw new InvalidNativeHandle("Native handle is null");
        }

        if (!User32.INSTANCE.IsWindow(embedded)) {
            NativeError.throwWhenCodeIsNonZero("Incorrect window handle, used IsWindow(...)", Kernel32.INSTANCE.GetLastError());
        }
    }

    private void loadNativeHandleOfEmbedder() {
        // need resolve own HWND everytime, because the Canvas component binds to different native handle everytime the underlying "addNotify()" is called

        embedder = new HWND(Native.getComponentPointer(this));
    }
}
