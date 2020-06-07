package com.s0n1.screensnap.ui;

import com.s0n1.screensnap.App;

import java.awt.*;

/**
 * UI资源常量
 */
public class UiRes {
    // 程序名称
    public final static String APP_NAME = "ScreenSnap";
    public final static String CHANGE_HOTKEY = "Change Hotkey";
    public final static String COLOR_PICKER = "Color Picker";
    public final static String ABOUT = "About";
    public final static String ABOUT_URL = "https://github.com/Edsuns/ScreenSnap";
    public final static String EXIT = "Exit";
    public final static String CHANGE = "Change";
    public final static String RUN_IN_BG = "Run in background";
    public final static String HOTKEY = "Hotkey: ";
    public final static String APPLY = "Apply";
    public final static String TIPS_SET_HOTKEY = "Type new global hotkey.";
    public final static String NO_QRCODE = "No QRCode";
    public final static String COPIED = " Copied";

    // 程序图标
    public static final Image APP_ICON = Toolkit.getDefaultToolkit()
            .getImage(App.class.getResource("/icon/AppIcon.png"));
    public static final Image COPY_ICON = Toolkit.getDefaultToolkit()
            .getImage(App.class.getResource("/icon/copy.png"));

    // 显示缩放(DPI缩放)比例
    public static final float DPI_SCALE_RATE = Toolkit.getDefaultToolkit().getScreenResolution() / 96f;

    public static final Color COLOR_CAPTURE_LIGHT = new Color(0xFFE70B);
    public static final Color COLOR_CAPTURE_DARK = new Color(0x2C251B);
    public static final Color COLOR_GRAY_LIGHT = new Color(0xCCC7C7);
}
