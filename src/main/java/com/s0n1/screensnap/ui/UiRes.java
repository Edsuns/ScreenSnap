package com.s0n1.screensnap.ui;

import com.s0n1.screensnap.App;

import java.awt.*;

/**
 * UI资源常量
 */
public class UiRes {
    // 程序名称
    public final static String APP_NAME = "ScreenSnap";

    // 程序图标
    public static final Image APP_ICON = Toolkit.getDefaultToolkit()
            .getImage(App.class.getResource("/icon/AppIcon.png"));

    // 显示缩放(DPI缩放)比例
    public static final float DPI_SCALE_RATE = Toolkit.getDefaultToolkit().getScreenResolution() / 96f;

    public static final int WINDOW_WIDTH = (int) (400 * DPI_SCALE_RATE);
    public static final int WINDOW_HEIGHT = (int) (400 * DPI_SCALE_RATE);
}
