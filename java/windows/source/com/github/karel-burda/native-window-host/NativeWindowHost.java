package com.github.karelburda.nativewindowhost;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import java.awt.Canvas;
import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.karelburda.nativewindowhost.exceptions.InvalidNativeHandle;
import com.github.karelburda.nativewindowhost.exceptions.NativeError;

/**
 * Component displays native window handle within itself (hosting or "embeddeding").
 * The NativeWindowHost can display most of native windows (currentl win32) windows using Canvas component from SWT.
 * @see demo/Main.java
 */
public class NativeWindowHost extends Canvas {
    private HWND handleHosted = null;
    private HWND hostedParent = null;
    private HWND handleSelf = null;
    private AtomicBoolean isHostingNativeWindow = new AtomicBoolean(false);

    /**
     * @param nativeWindowClassName string with win32 window class name.
     */
    public NativeWindowHost(final String nativeWindowClassName) throws InvalidNativeHandle {
        this(User32.INSTANCE.FindWindow(null, nativeWindowClassName));
    }

    /**
     * @param nativeWindowHandle handle (HWND) of a window to be hosted/embedded.
     */
    public NativeWindowHost(final HWND nativeWindowHandle) throws InvalidNativeHandle {
        handleHosted = nativeWindowHandle;

        checkNativeHandleOfHosted();
    }

    private NativeWindowHost() {
    }

    /**
     * Releases the underlying handle by setting its parent to the previous handle (previous parent).
     */
    public void release() throws NativeError {
        if (isHostingNativeWindow.get()) {
            if (User32.INSTANCE.SetParent(handleHosted, hostedParent) == null) {
                NativeError.throwWhenCodeIsNonZero("Failure when trying to call SetParent(...)", Kernel32.INSTANCE.GetLastError());
            }

            isHostingNativeWindow.set(false);
        }
    }

    @Override
    public void paint(final Graphics graphics) throws NativeError {
        super.paint(graphics);

        host();

        if (!User32.INSTANCE.MoveWindow(handleHosted, 0, 0, getWidth(), getHeight(), true)) {
            NativeError.throwWhenCodeIsNonZero ("Failure when trying to call MoveWindow(...)",Kernel32.INSTANCE.GetLastError());
        }
    }

    private void host() throws NativeError {
        if (!isHostingNativeWindow.get()) {
            loadNativeHandleOfSelf();

            if (handleHosted != null && handleSelf != null) {
                // setting WS_CHILD to the embedded window proved useless during empiric tests

                hostedParent = User32.INSTANCE.GetAncestor(handleHosted, User32.GA_PARENT);

                if (User32.INSTANCE.SetParent(handleHosted, handleSelf) == null) {
                    NativeError.throwWhenCodeIsNonZero("Failure when trying to call SetParent(...)", Kernel32.INSTANCE.GetLastError());
                }

                if (!User32.INSTANCE.ShowWindow(handleHosted, User32.INSTANCE.SW_SHOW)) {
                    NativeError.throwWhenCodeIsNonZero("Failure when trying to call ShowWindow(...)", Kernel32.INSTANCE.GetLastError());
                }

                isHostingNativeWindow.set(true);

                repaint();
            }
        }
    }

    private void checkNativeHandleOfHosted() throws NativeError, InvalidNativeHandle {
        if (handleHosted == null) {
            throw new InvalidNativeHandle("Native handle is null");
        }

        if (!User32.INSTANCE.IsWindow(handleHosted)) {
            NativeError.throwWhenCodeIsNonZero("Incorrect window handle, used IsWindow(...)", Kernel32.INSTANCE.GetLastError());
        }
    }

    private void loadNativeHandleOfSelf() {
        // need resolve own HWND everytime, because the Canvas component binds to different native handle everytime the underlying "addNotify()" is called

        handleSelf = new HWND(Native.getComponentPointer(this));
    }
}
