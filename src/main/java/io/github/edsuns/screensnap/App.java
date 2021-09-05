package io.github.edsuns.screensnap;

import com.google.zxing.Result;
import io.github.edsuns.screensnap.tools.GlobalHotKey;
import io.github.edsuns.screensnap.tools.Settings;
import io.github.edsuns.screensnap.ui.*;
import io.github.edsuns.screensnap.util.FrameUtil;
import io.github.edsuns.screensnap.util.QrCodeUtil;
import io.github.edsuns.screensnap.util.Util;
import io.github.edsuns.screensnap.widget.Application;
import io.github.edsuns.screensnap.widget.Toast;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;

import static io.github.edsuns.screensnap.ui.UiRes.APP_ICON;

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
        System.out.println("isWindows: " + FrameUtil.OS_NAME.contains("windows"));
        System.out.println("isOldVersionJava: " + FrameUtil.isOldVersionJava);
        System.out.println("DPI Scale: " + FrameUtil.DPI_SCALE);

        // 开始检查程序是否被应用了DPI缩放
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        boolean hasDPIScale = !FrameUtil.isOldVersionJava && screenSize.height != FrameUtil.getScreenHeight();
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
        // 加载设置包括热键和语言
        Settings.load();
        // 设置快捷键回调
        GlobalHotKey.getInstance().setHotKeyListener(this::showShot);

        // 设置系统默认UI
        FrameUtil.setSystemLookAndFeel();

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
                    Toast.getInstance().show(Application.res().getString("no_bar_code"),
                            Toast.DELAY_DEFAULT);
                } else {
                    Util.copyText(result.getText());
                    Toast.getInstance().show(Application.res().getString("content_copied"), Toast.DELAY_DEFAULT);
                }
            }

            @Override
            public void onLeftCapture(BufferedImage image) {
                pictureJFrame.showPicture(image);
            }
        });

        // 设置热键的对话框
        hotkeyDialog = new HotkeyDialog(homeFrame);
        hotkeyDialog.setTitle(res().getString("change_hotkey"));
        hotkeyDialog.setIconImage(APP_ICON);

        colorJFrame = new CopyColorJFrame();
        colorJFrame.setIconImage(APP_ICON);
        colorJFrame.setTitle(res().getString("color_picker"));
        colorJFrame.setPickAnotherCallback(this::showShot);

        // 图片查看界面
        pictureJFrame = new PictureJFrame();
        pictureJFrame.setIconImage(APP_ICON);
        pictureJFrame.setTitle(res().getString("picture_viewer"));

        // 初始化主界面
        homeFrame = new HomeJFrame(hotkeyDialog);
        homeFrame.setIconImage(APP_ICON);
        homeFrame.setTitle(res().getString("app_name"));
        homeFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onAppClose(Settings.isRunInBg());
            }
        });
        FrameUtil.setCenterLocation(homeFrame);
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
