package com.s0n1.capturer.util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Edsuns@qq.com on 2020-05-25
 */
public final class ScreenUtil {
    public static DisplayMode getDisplay() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
    }

    public static BufferedImage createScreenShot(Rectangle area) throws AWTException {
        return new Robot().createScreenCapture(area);
    }

    public static Color getMousePixelColor(int x, int y) throws AWTException {
        return new Robot().getPixelColor(x, y);
    }
}
