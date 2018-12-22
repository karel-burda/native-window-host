package com.github.karelburda.nativewindowembedder.demo;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.github.karelburda.nativewindowembedder.NativeWindowEmbedder;

public class Main {
    private static void showUsage() {
        //JFrame.setSystemLookAndFeelDecorated(true);

        final JFrame frame = new JFrame();
        frame.setSize(1200, 700);
        frame.getContentPane().setBackground(Color.RED);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        final JDialog information = new JDialog(frame, "Demo information", true);
        //information.setLayout();
        information.add(new JLabel("Make sure that the win32-demo-helper-app.exe (has dark grey background) is up and running and window is visible.\n Then close this window"));
        information.setSize(800,100);
        information.setVisible(true);

        HWND hwndOfDemoWindow = User32.INSTANCE.FindWindow(null, "win32-demo-helper-app");
        System.out.println(hwndOfDemoWindow);

        NativeWindowEmbedder embedder = new NativeWindowEmbedder(hwndOfDemoWindow);
        embedder.setSize(frame.getWidth(), frame.getHeight());
        embedder.setVisible(true);
        //embedder.setBackground(Color.BLACK);

        frame.add(embedder, BorderLayout.CENTER);

        System.out.println("showUsage END");
    }

    public static void main(String[] args) {
        showUsage();
    }
}
