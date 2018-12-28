package com.github.karelburda.nativewindowembedder.exceptions;

public class NativeError extends RuntimeException {
    public static void throwWhenCodeIsNonZero(final String message, final int code) {
        if (code != 0) {
            throw new NativeError(message + ", native error code: " + code);
        }
    }

    private NativeError(final String message) {
        super(message);
    }
}
