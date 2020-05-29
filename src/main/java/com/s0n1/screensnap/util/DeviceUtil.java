package com.s0n1.screensnap.util;

import java.awt.*;

/**
 * Created by Edsuns@qq.com on 2020-05-25
 */
public final class DeviceUtil {
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    private static final String JAVA_VERSION = System.getProperty("java.version");
    public static final boolean isOldVersionJava = Float.parseFloat(JAVA_VERSION.substring(0, 3)) < 1.9;
    public static final boolean isWindows = OS_NAME.contains("windows");
    public static DisplayMode displayMode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();

    // 屏幕宽高
    public static int SCREEN_WIDTH = displayMode.getWidth();
    public static int SCREEN_HEIGHT = displayMode.getHeight();

    public static void updateDisplayMode() {
        displayMode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        SCREEN_WIDTH = displayMode.getWidth();
        SCREEN_HEIGHT = displayMode.getHeight();
    }
}
