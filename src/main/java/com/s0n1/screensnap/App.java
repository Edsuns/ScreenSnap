package com.s0n1.screensnap;

import com.s0n1.screensnap.tools.GlobalHotKey;
import com.s0n1.screensnap.ui.ShotJFrame;
import com.s0n1.screensnap.util.ColorUtil;
import com.s0n1.screensnap.util.DeviceUtil;

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
//        DisplayMode displayMode = DeviceUtil.getDisplay();
//        Rectangle rectangle = new Rectangle(displayMode.getWidth(), displayMode.getHeight());
//        BufferedImage screenShot = null;
//        try {
//            screenShot = new Robot().createScreenCapture(rectangle);
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
        System.out.println("isOldVersionJava: " + DeviceUtil.isOldVersionJava);
    }

    public App() {
        System.out.println("App construction started.");
        // 设置系统默认样式
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // 初始化取色界面
        ShotJFrame shotJFrame = new ShotJFrame();
        shotJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        shotJFrame.setPickColorListener(color -> {
            System.out.println("RGB: " + ColorUtil.getColorText(color, ColorUtil.ColorMode.RGB));
            System.out.println("HEX: " + ColorUtil.getColorText(color, ColorUtil.ColorMode.HEX));
        });

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
                System.out.println("closed");
            }
        });
        homeFrame.setSize(600, 400);
    }
}
