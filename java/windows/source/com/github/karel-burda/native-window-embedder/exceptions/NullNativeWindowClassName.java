package com.github.karelburda.nativewindowembedder.exceptions;

public class NullNativeWindowClassName extends NullPointerException {
    public NullNativeWindowClassName() {
        super("Class name of native window is set is null");
    }
}
