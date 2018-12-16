package com.github.karelburda.nativewindowembedder.demo;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;
import java.awt.Color;
import javax.swing.JFrame;

import com.github.karelburda.nativewindowembedder.NativeWindowEmbedder;

public class Main {
    private static void showUsage() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        final JFrame frame = new JFrame();
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        final JFrame frameToBeEmbedded = new JFrame();
        frameToBeEmbedded.setSize(600, 200);
        frameToBeEmbedded.getContentPane().setBackground(Color.YELLOW);
        frameToBeEmbedded.setVisible(true);

        NativeWindowEmbedder embedder = new NativeWindowEmbedder(new HWND(Native.getComponentPointer(frameToBeEmbedded)));
        embedder.setSize(600, 400);
        embedder.setVisible(true);

        frame.add(embedder);
    }

    public static void main(String[] args) {
        showUsage();
    }
}
