package com.s0n1.capturer.util;

import java.awt.*;

/**
 * Created by Edsuns@qq.com on 2020-05-25
 */
public final class DeviceUtil {
    private static String OS_NAME = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return OS_NAME.contains("windows");
    }

    public static DisplayMode getDisplay() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
    }

}
