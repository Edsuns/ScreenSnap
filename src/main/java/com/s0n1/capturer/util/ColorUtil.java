package com.s0n1.capturer.util;

import java.awt.*;

/**
 * Created by Edsuns@qq.com on 2020-05-26
 */
public final class ColorUtil {
    public enum ColorMode {
        RGB, HTML, HEX, HSB
    }// 颜色模式

    /**
     * 根据当前颜色模式显示颜色值
     *
     * @param c    源颜色
     * @param mode 颜色类型
     * @return 符合mode的颜色字符串
     */
    public static String getColorText(final Color c, ColorMode mode) {
        if (c == null) {
            return "";
        }
        String s = "";
        switch (mode) {
            case RGB:
                s = String.format("%d, %d, %d", c.getRed(), c.getGreen(), c.getBlue());
                break;
            case HTML:
                s = String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
                break;
            case HEX:
                s = String.format("0x%02X%02X%02X", c.getBlue(), c.getGreen(), c.getRed());
                break;
            case HSB:
                float[] hsbArr;
                hsbArr = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
                s = String.format("%3.0f%% %3.0f%% %3.0f%%", hsbArr[0] * 100, hsbArr[1] * 100, hsbArr[2] * 100);
                break;
            default:
                break;
        }
        return s;
    }
}
