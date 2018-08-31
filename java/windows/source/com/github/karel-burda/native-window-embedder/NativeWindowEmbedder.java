package com.github.karelburda.nativewindowembedder;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import java.awt.Canvas;
import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.karelburda.nativewindowembedder.exceptions.InvalidNativeHandle;
import com.github.karelburda.nativewindowembedder.exceptions.NullNativeWindowClassName;

public class NativeWindowEmbedder extends Canvas {
    private String nativeWindowId = null;
    private HWND embedded = null;
    private HWND embedder = null;
    private AtomicBoolean isHostingNativeWindow = new AtomicBoolean(false);

    private NativeWindowEmbedder() {
    }

    public NativeWindowEmbedder(final String nativeWindowClassName) throws NullNativeWindowClassName {
        nativeWindowId = nativeWindowClassName;

        if (nativeWindowId == null) {
            throw new NullNativeWindowClassName();
        }
    }

    @Override
    public void removeNotify() {
        // According to the WinAPI documentation, following should be called as well, but it doesn't work:
        // User32.INSTANCE.SetWindowLong(microcoreHwnd, User32.GWL_STYLE, User32.WS_POPUP);

        // todo: inspect return codes.
        User32.INSTANCE.ShowWindow(embedded, User32.INSTANCE.SW_HIDE);
        User32.INSTANCE.SetParent(embedded, null);

        isHostingNativeWindow.set(false);

        super.removeNotify();
    }

    @Override
    public void paint(final Graphics graphics) {
        super.paint(graphics);

        embedNativeWindow();

        User32.INSTANCE.MoveWindow(embedded, 0, 0, getWidth(), getHeight(), true);
    }

    private void embedNativeWindow() {
        if (!isHostingNativeWindow.get()) {
            loadNativeHandleOfEmbedded();
            loadNativeHandleOfEmbedder();

            if (embedded != null && embedder != null) {
                // the following should be called according to the WinAPI as well, but doesn't work
                // User32.INSTANCE.SetWindowLong(microcoreHwnd, User32.GWL_STYLE, User32.WS_CHILD);

                User32.INSTANCE.SetParent(embedded, embedder);
                User32.INSTANCE.ShowWindow(embedded, User32.INSTANCE.SW_SHOW);

                isHostingNativeWindow.set(true);

                repaint();
            }
        }
    }

    private void loadNativeHandleOfEmbedded() throws InvalidNativeHandle {
        if (embedded == null) {
            embedded = getHwndOfNativeWindow(nativeWindowId);
        }

        if (embedded == null) {
            throw new InvalidNativeHandle("Cannot get native handle of the window to be embedded");
        }
    }

    private HWND loadNativeHandleOfEmbedder() {
        // we need to resolve own HWND everytime, because the Canvas component binds to different native handle
        // everytime the underlying "addNotify()" is called
        HWND hwnd = null;

        try {
            hwnd = new HWND(Native.getComponentPointer(this));
        } catch (final Exception exception) {
            // todo, component might not be on the screen, etc.
        }

        return hwnd;
    }

    private HWND getHwndOfNativeWindow(final String windowClassName) {
        return User32.INSTANCE.FindWindow(windowClassName, null);
    }
}
