package com.s0n1.screensnap.util;

import com.s0n1.screensnap.widget.ImageTransferable;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Edsuns@qq.com on 2020-05-26
 */
public final class Util {
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
                s = String.format("%d,%d,%d", c.getRed(), c.getGreen(), c.getBlue());
                break;
            case HEX:
            case HTML:
                s = String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
                break;
            case HSB:
                float[] hsbArr;
                hsbArr = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
                s = String.format("%.0f%% %.0f%% %.0f%%", hsbArr[0] * 100, hsbArr[1] * 100, hsbArr[2] * 100);
                break;
            default:
                break;
        }
        return s;
    }

    public static void copyText(String text) {
        // 封装文本内容
        Transferable trans = new StringSelection(text);
        // 把文本内容设置到系统剪贴板
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
    }

    public static void copyImage(Image image) {
        Transferable trans = new ImageTransferable(image);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
    }

    public static void setCenterLocation(Window window) {
        window.setLocation((DeviceUtil.getScreenWidth() - window.getWidth()) / 2, (DeviceUtil.getScreenHeight() - window.getHeight()) / 2);
    }

    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        return dateFormat.format(new Date());
    }
}
