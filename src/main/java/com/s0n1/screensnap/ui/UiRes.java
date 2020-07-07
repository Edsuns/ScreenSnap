package com.s0n1.screensnap.ui;

import java.awt.*;

/**
 * UI资源常量
 */
public class UiRes {
    public static final String VERSION = "v1.1";
    public static final String ABOUT_URL = "https://github.com/Edsuns/ScreenSnap";
    public static final String ORIGIN = "1:1";

    // 程序图标
    public static final Image APP_ICON = Toolkit.getDefaultToolkit()
            .getImage(UiRes.class.getResource("/icon/launcher.png"));
    public static final Image COPY_ICON = Toolkit.getDefaultToolkit()
            .getImage(UiRes.class.getResource("/icon/copy.png"));

    public static final Color COLOR_CAPTURE_LIGHT = new Color(0xFFE70B);
    public static final Color COLOR_CAPTURE_DARK = new Color(0x2C251B);
    public static final Color COLOR_GRAY_LIGHT = new Color(0xCCC7C7);
}
