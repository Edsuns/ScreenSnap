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

    public static int getScreenWidth() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
    }

    public static int getScreenHeight() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
    }
}
