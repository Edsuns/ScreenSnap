package com.s0n1.screensnap;

import com.google.zxing.Result;
import com.s0n1.screensnap.tools.GlobalHotKey;
import com.s0n1.screensnap.tools.Settings;
import com.s0n1.screensnap.ui.HomeJFrame;
import com.s0n1.screensnap.ui.HotkeyDialog;
import com.s0n1.screensnap.ui.ShotJFrame;
import com.s0n1.screensnap.util.AppUtil;
import com.s0n1.screensnap.util.DeviceUtil;
import com.s0n1.screensnap.util.QrCodeUtil;
import com.s0n1.screensnap.widget.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
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
public class App extends Application {
    private HomeJFrame homeFrame;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                App app = new App();
                app.homeFrame.setVisible(true);
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
            // 有DPI缩放抛出错误. 必须关闭DPI缩放自己适配高DPI. 修复截图模糊问题
            throw new Error("Need disable DPI Scale by VM option: -Dsun.java2d.uiScale=1");
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
        shotJFrame.setPickColorListener(new ShotJFrame.PickColorListener() {
            @Override
            public void onColorPicked(Color color) {
                System.out.println("-----COLOR-----");
                System.out.println("RGB: " + AppUtil.getColorText(color, AppUtil.ColorMode.RGB));
                System.out.println("HEX: " + AppUtil.getColorText(color, AppUtil.ColorMode.HEX));
            }

            @Override
            public void onRightCapture(BufferedImage image) {
                System.out.println("onRightCapture");
                Result result = QrCodeUtil.parseQrCode(image);
                if (result != null) {
                    System.out.println("resultFormat: " + result.getBarcodeFormat());
                    System.out.println("resultText: " + result.getText());
                }
            }

            @Override
            public void onLeftCapture(BufferedImage image) {
                System.out.println("onLeftCapture");
            }
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

    @Override
    public void onAppClose(boolean runInBg) {
        if (runInBg) {
            homeFrame.enableRunInBg();
            homeFrame.setVisible(false);
        } else {
            GlobalHotKey.getInstance().stopHotKey();
            homeFrame.disableTrayIcon();
            System.out.println("App closed.");
            System.exit(0);
        }
    }
}
