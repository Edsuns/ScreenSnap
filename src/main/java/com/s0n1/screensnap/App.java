package com.s0n1.screensnap;

import com.s0n1.screensnap.tools.GlobalHotKey;
import com.s0n1.screensnap.tools.Settings;
import com.s0n1.screensnap.ui.HomeJFrame;
import com.s0n1.screensnap.ui.HotkeyDialog;
import com.s0n1.screensnap.ui.ShotJFrame;
import com.s0n1.screensnap.util.AppUtil;
import com.s0n1.screensnap.util.DeviceUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;

import static com.s0n1.screensnap.ui.UiRes.*;
import static com.s0n1.screensnap.util.DeviceUtil.SCREEN_HEIGHT;
import static com.s0n1.screensnap.util.DeviceUtil.SCREEN_WIDTH;

/**
 * Main Entrance
 * Created by Edsuns@qq.com on 2020-05-25
 */
public class App {
    private HomeJFrame homeFrame;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                App window = new App();
                window.homeFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private App() {
        try {// 让程序只能单次运行
            FileLock lock = new FileOutputStream("./SingleRunLock").getChannel().tryLock();
            if (lock == null) {
                System.out.println("App is already running...");
                System.exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("App started.");
        System.out.println("isWindows: " + DeviceUtil.isWindows);
        System.out.println("isOldVersionJava: " + DeviceUtil.isOldVersionJava);
        System.out.println("DPI Scale: " + DPI_SCALE_RATE);

        // 开始检查DPI缩放是否开启
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        boolean hasDPIScale = !DeviceUtil.isOldVersionJava && screenSize.height != DeviceUtil.displayMode.getHeight();
        if (hasDPIScale) {
            // 有DPI缩放抛出错误；必须关闭DPI缩放自己适配高DPI,修复截图模糊问题
            throw new Error("Need disable DPI Scale by VM option: -Dsun.java2d.uiScale=1");
        }
        init();
    }

    private void init() {
        instance = this;
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
            System.out.println("RGB: " + AppUtil.getColorText(color, AppUtil.ColorMode.RGB));
            System.out.println("HEX: " + AppUtil.getColorText(color, AppUtil.ColorMode.HEX));
        });

        // 设置热键的对话框
        HotkeyDialog dialog = new HotkeyDialog(homeFrame);
        dialog.setIconImage(APP_ICON);

        Settings.loadSettings();
        // 实例化快捷键注册模块
        GlobalHotKey.newInstance();
        // 设置取色快捷键回调
        GlobalHotKey.getInstance().setHotKeyListener(() -> {
            dialog.dispose();
            shotJFrame.startShot();
        });

        // 初始化主界面
        homeFrame = new HomeJFrame(dialog);
        homeFrame.setTitle(APP_NAME);
        homeFrame.setIconImage(APP_ICON);
        homeFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onAppClose(Settings.isRunInBg());
            }
        });
        // 在屏幕中间显示
        homeFrame.setBounds((SCREEN_WIDTH - WINDOW_WIDTH) / 2,
                (SCREEN_HEIGHT - WINDOW_HEIGHT) / 2, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public void onAppClose(boolean backgroundRun) {
        if (backgroundRun) {
            homeFrame.enableRunInBg();
            homeFrame.setVisible(false);
        } else {
            GlobalHotKey.getInstance().stopHotKey();
            homeFrame.disableTrayIcon();
            System.out.println("App closed.");
            System.exit(0);
        }
    }

    private static App instance;

    public static App getInstance() {
        return instance;
    }
}
