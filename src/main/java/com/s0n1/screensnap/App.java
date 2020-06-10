package com.s0n1.screensnap;

import com.google.zxing.Result;
import com.s0n1.screensnap.tools.GlobalHotKey;
import com.s0n1.screensnap.tools.Settings;
import com.s0n1.screensnap.ui.*;
import com.s0n1.screensnap.util.DeviceUtil;
import com.s0n1.screensnap.util.QrCodeUtil;
import com.s0n1.screensnap.util.Util;
import com.s0n1.screensnap.widget.Application;
import com.s0n1.screensnap.widget.Toast;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.Enumeration;

import static com.s0n1.screensnap.ui.UiRes.*;

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
        boolean hasDPIScale = !DeviceUtil.isOldVersionJava && screenSize.height != DeviceUtil.getScreenHeight();
        if (hasDPIScale) {
            // DPI缩放会导致截图模糊，要求关闭DPI缩放，自己适配高DPI
            throw new Error("Need disable DPI Scale by VM option: -Dsun.java2d.uiScale=1");
        }
        init();
    }

    private ShotJFrame shotJFrame;
    private HotkeyDialog hotkeyDialog;
    private CopyColorJFrame colorJFrame;
    private PictureJFrame pictureJFrame;

    private void init() {
        // 设置系统默认样式
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        if (!DeviceUtil.isOldVersionJava) {
            // 缩放字体大小，要在设置样式后
            Enumeration<Object> keys = UIManager.getDefaults().keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                if (value instanceof FontUIResource) {
                    FontUIResource resource = (FontUIResource) value;
                    UIManager.put(key,
                            new FontUIResource(resource.deriveFont(resource.getSize() * DPI_SCALE_RATE)));
                }
            }
        }

        // 初始化取色界面
        shotJFrame = new ShotJFrame();
        shotJFrame.setPickColorListener(new ShotJFrame.PickColorListener() {
            @Override
            public void onColorPicked(Color color) {
                colorJFrame.showCopy(color);
            }

            @Override
            public void onRightCapture(BufferedImage image) {
                Result result = QrCodeUtil.parseQrCode(image);
                if (result == null) {
                    Toast.getInstance().show(NO_QRCODE, Toast.DELAY_DEFAULT);
                } else {
                    String format = result.getBarcodeFormat().toString();
                    Util.copyText(result.getText());
                    Toast.getInstance().show(format + COPIED, Toast.DELAY_DEFAULT);
                }
            }

            @Override
            public void onLeftCapture(BufferedImage image) {
                pictureJFrame.showPicture(image);
            }
        });

        // 设置热键的对话框
        hotkeyDialog = new HotkeyDialog(homeFrame);
        hotkeyDialog.setTitle(CHANGE_HOTKEY);
        hotkeyDialog.setIconImage(APP_ICON);

        // 加载设置包括热键
        Settings.load();
        // 设置快捷键回调
        GlobalHotKey.getInstance().setHotKeyListener(this::showShot);

        colorJFrame = new CopyColorJFrame();
        colorJFrame.setIconImage(APP_ICON);
        colorJFrame.setTitle(COLOR_PICKER);
        colorJFrame.setPickAnotherCallback(this::showShot);

        // 图片查看界面
        pictureJFrame = new PictureJFrame();
        pictureJFrame.setIconImage(APP_ICON);
        pictureJFrame.setTitle(PICTURE_VIEWER);

        // 初始化主界面
        homeFrame = new HomeJFrame(hotkeyDialog);
        homeFrame.setIconImage(APP_ICON);
        homeFrame.setTitle(APP_NAME);
        homeFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onAppClose(Settings.isRunInBg());
            }
        });
        Util.setCenterLocation(homeFrame);
    }

    /**
     * 清除障碍并显示屏幕截取功能
     */
    private void showShot() {
        hotkeyDialog.dispose();
        colorJFrame.setVisible(false);
        Toast.getInstance().setVisible(false);
        shotJFrame.startShot();
    }

    @Override
    public void onAppClose(boolean runInBg) {
        if (runInBg) {
            homeFrame.enableRunInBg();
            homeFrame.setVisible(false);
        } else {
            GlobalHotKey.getInstance().stopHotKey();
            homeFrame.disableRunInBg();
            System.out.println("App closed.");
            System.exit(0);
        }
    }
}
