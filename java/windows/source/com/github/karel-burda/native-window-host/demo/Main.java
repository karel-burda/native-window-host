package com.github.karelburda.nativewindowhost.demo;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.github.karelburda.nativewindowhost.NativeWindowHost;

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

        final NativeWindowHost host = new NativeWindowHost("win32-demo-helper-app");
        host.setSize(frame.getWidth(), frame.getHeight());
        host.setVisible(true);

        frame.add(host, BorderLayout.CENTER);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent event) {
                host.release();

                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        showUsage();
    }
}
