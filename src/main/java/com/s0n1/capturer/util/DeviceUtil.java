package com.s0n1.capturer.util;

import java.awt.*;

/**
 * Created by Edsuns@qq.com on 2020-05-25
 */
public final class DeviceUtil {
    private static String OS_NAME = System.getProperty("os.name").toLowerCase();
    private static String JAVA_VERSION = System.getProperty("java.version");
    public static boolean isOldVersionJava = Float.parseFloat(JAVA_VERSION.substring(0, 3)) < 1.9;
    public static boolean isWindows = OS_NAME.contains("windows");

    public static DisplayMode getDisplay() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
    }

}
