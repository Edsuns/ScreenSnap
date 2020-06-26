package com.s0n1.screensnap.ui;

import com.s0n1.screensnap.App;

import java.awt.*;

/**
 * UI资源常量
 */
public class UiRes {
    public final static String ABOUT_URL = "https://github.com/Edsuns/ScreenSnap";
    public final static String ORIGIN = "1:1";

    // 程序图标
    public static final Image APP_ICON = Toolkit.getDefaultToolkit()
            .getImage(App.class.getResource("/icon/AppIcon.png"));
    public static final Image COPY_ICON = Toolkit.getDefaultToolkit()
            .getImage(App.class.getResource("/icon/copy.png"));

    public static final Color COLOR_CAPTURE_LIGHT = new Color(0xFFE70B);
    public static final Color COLOR_CAPTURE_DARK = new Color(0x2C251B);
    public static final Color COLOR_GRAY_LIGHT = new Color(0xCCC7C7);
}
