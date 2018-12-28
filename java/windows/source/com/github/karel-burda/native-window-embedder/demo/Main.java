package com.github.karelburda.nativewindowembedder.demo;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.github.karelburda.nativewindowembedder.NativeWindowEmbedder;

public class Main {
    private static void showUsage() {
        final JFrame frame = new JFrame();
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        final JDialog information = new JDialog(frame, "Demo information", true);
        String label = "<html>Make sure that the win32-demo-helper-app.exe (window has dark grey background) is up and running and window is visible.<br />" +
                       "Then close this window.<br />" +
                       "Native window from the win32-demo-helper-app.exe will be embedded inside the Canvas.</html>";
        information.add(new JLabel(label));
        information.setSize(800,100);
        information.setVisible(true);

        final NativeWindowEmbedder embedder = new NativeWindowEmbedder("win32-demo-helper-app");
        embedder.setSize(frame.getWidth(), frame.getHeight());
        embedder.setVisible(true);

        frame.add(embedder, BorderLayout.CENTER);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent event) {
                embedder.release();

                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        showUsage();
    }
}
