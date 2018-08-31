package com.github.karelburda.nativewindowembedder.demo;

import javax.swing.JFrame;

import com.github.karelburda.nativewindowembedder.NativeWindowEmbedder;

public class Main {
    private static void showUsage() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        final JFrame frame = new JFrame();
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        NativeWindowEmbedder embedder = new NativeWindowEmbedder("foo");
        embedder.setSize(600, 400);
        embedder.setVisible(true);

        frame.add(embedder);
    }

    public static void main(String[] args) {
        showUsage();
    }
}
