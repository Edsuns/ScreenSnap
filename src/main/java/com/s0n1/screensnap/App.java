package com.s0n1.screensnap;

import com.s0n1.screensnap.tools.GlobalHotKey;
import com.s0n1.screensnap.ui.HomeJFrame;
import com.s0n1.screensnap.ui.ShotJFrame;
import com.s0n1.screensnap.util.ColorUtil;
import com.s0n1.screensnap.util.DeviceUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.s0n1.screensnap.ui.UiRes.*;
import static com.s0n1.screensnap.util.DeviceUtil.SCREEN_HEIGHT;
import static com.s0n1.screensnap.util.DeviceUtil.SCREEN_WIDTH;

/**
 * Main Entrance
 * Created by Edsuns@qq.com on 2020-05-25
 */
public class App {
    // 实例化快捷键注册模块
    GlobalHotKey globalHotKey = new GlobalHotKey();
    private HomeJFrame homeFrame;

    public static void main(String[] args) {
        System.out.println("App started.");
        System.out.println("isWindows: " + DeviceUtil.isWindows);
        System.out.println("isOldVersionJava: " + DeviceUtil.isOldVersionJava);
        System.out.println("DPI Scale: " + DPI_SCALE_RATE);

        EventQueue.invokeLater(() -> {
            try {
                App window = new App();
                window.homeFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public App() {
        System.out.println("App construction started.");

        // 开始检查DPI缩放是否开启
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        boolean hasDPIScale = !DeviceUtil.isOldVersionJava && screenSize.height != DeviceUtil.displayMode.getHeight();
        if (hasDPIScale) {
            // 有DPI缩放抛出异常；必须关闭DPI缩放，自己适配高DPI以修复截图模糊问题
            throw new RuntimeException("Need disable DPI Scale by VM option: -Dsun.java2d.uiScale=1");
        }
        init();
    }

    private void init() {
        // 设置系统默认样式
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // 初始化取色界面
        ShotJFrame shotJFrame = new ShotJFrame();
        shotJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        shotJFrame.setPickColorListener(color -> {
            System.out.println("-----COLOR-----");
            System.out.println("RGB: " + ColorUtil.getColorText(color, ColorUtil.ColorMode.RGB));
            System.out.println("HEX: " + ColorUtil.getColorText(color, ColorUtil.ColorMode.HEX));
        });

        // 设置取色快捷键回调
        globalHotKey.setHotKeyListener(shotJFrame::startShot);

        // 初始化主界面
        homeFrame = new HomeJFrame();
        homeFrame.setTitle(APP_NAME);
        homeFrame.setIconImage(APP_ICON);
        homeFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        homeFrame.setTrayItemListener(() -> onAppClose(false));
        homeFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onAppClose(true);
            }
        });
        // 在屏幕中间显示
        homeFrame.setBounds((SCREEN_WIDTH - WINDOW_WIDTH) / 2,
                (SCREEN_HEIGHT - WINDOW_HEIGHT) / 2, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    private void onAppClose(boolean backgroundRun) {
        if (backgroundRun) {
            homeFrame.setVisible(false);
        } else {
            globalHotKey.stopHotKey();
            System.out.println("App closed.");
            System.exit(0);
        }
    }
}
