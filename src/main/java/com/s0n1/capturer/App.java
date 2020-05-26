package com.s0n1.capturer;

import com.s0n1.capturer.tools.GlobalHotKey;
import com.s0n1.capturer.ui.ShotJFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main Entrance
 * Created by Edsuns@qq.com on 2020-05-25
 */
public class App {
    private JFrame frame;

    public static void main(String[] args) {
        System.out.println("App started.");
        EventQueue.invokeLater(() -> {
            try {
                App window = new App();
//                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
//        DisplayMode displayMode = ScreenUtil.getDisplay();
//        Rectangle rectangle = new Rectangle(displayMode.getWidth(), displayMode.getHeight());
//        BufferedImage screenShot = null;
//        try {
//            screenShot = ScreenUtil.createScreenShot(rectangle);
//        } catch (AWTException e) {
//            e.printStackTrace();
//        }
//        if (screenShot != null) {
//            try {
//                ImageIO.write(screenShot, "PNG", new File("Capture.png"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        System.out.println(width + " " + height);

    }

    public App() {
        System.out.println("App construct.");
        init();
    }

    private void init() {
//            ShotJFrame jFrame = new ShotJFrame();
//            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            jFrame.addWindowListener(new WindowAdapter() {
//                @Override
//                public void windowClosing(WindowEvent e) {
//                    System.out.println("closing");
//                }
//            });
//            jFrame.showShot();

//        GlobalHotKey globalHotKey = new GlobalHotKey();
//        JFrame jFrame = new JFrame();
//        jFrame.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                System.out.println("closing");
//                globalHotKey.stopHotKey();
//            }
//        });
//        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        jFrame.setVisible(true);
    }
}
