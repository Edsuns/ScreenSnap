package com.s0n1.capturer;

import com.s0n1.capturer.tools.GlobalHotKey;
import com.s0n1.capturer.ui.ShotJFrame;
import com.s0n1.capturer.util.DeviceUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main Entrance
 * Created by Edsuns@qq.com on 2020-05-25
 */
public class App {
    private JFrame homeFrame;

    public static void main(String[] args) {
        System.out.println("App started.");
        EventQueue.invokeLater(() -> {
            try {
                App window = new App();
                window.homeFrame.setVisible(true);
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

//        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
//        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
//        System.out.println(width + " " + height);
        System.out.println("isWindows: " + DeviceUtil.isWindows);
        System.out.println("OldVersionJava: " + DeviceUtil.isOldVersionJava);
    }

    public App() {
        System.out.println("App construction started.");

        // 初始化取色界面
        ShotJFrame shotJFrame = new ShotJFrame();
        shotJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 启动快捷键注册模块
        GlobalHotKey globalHotKey = new GlobalHotKey();
        // 设置取色快捷键回调
        globalHotKey.setHotKeyListener(shotJFrame::startShot);

        // 初始化主界面
        homeFrame = new JFrame();
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("closing");
                globalHotKey.stopHotKey();
            }
        });
        homeFrame.setSize(600, 400);
    }
}
