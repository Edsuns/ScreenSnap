package io.github.edsuns.screensnap.util;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import io.github.edsuns.screensnap.ui.MyGDI32;
import io.github.edsuns.screensnap.widget.ImageTransferable;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
     * @return 符合 mode 的颜色字符串
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

    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        return dateFormat.format(new Date());
    }


    public static Color getColorFromScreenPixel(int x, int y) {
        WinDef.HDC hdcTarget = User32.INSTANCE.GetDC(null);
        if (hdcTarget == null) {
            throw new Win32Exception(Native.getLastError());
        }
        try {
            int color = MyGDI32.INSTANCE.GetPixel(hdcTarget, x, y);
            int red = color & 0X0000ff;
            int green = (color & 0x00ff00) >> 8;
            int blue = (color & 0xff0000) >> 16;
            return new Color(red, green, blue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (hdcTarget != null) {
                if (0 == User32.INSTANCE.ReleaseDC(null, hdcTarget)) {
                    throw new IllegalStateException("Device context did not release properly.");
                }
            }
        }

    }


    public static List<WinUser.RECT> getAllScreenRectangles() {
        List<WinUser.RECT> screenRectangles = new ArrayList<>();

        User32.INSTANCE.EnumDisplayMonitors(null, null, (hMonitor, hdc, rect, lparam) -> {
            WinUser.MONITORINFO monitorinfo = new WinUser.MONITORINFO();
            WinDef.BOOL bool = User32.INSTANCE.GetMonitorInfo(hMonitor, monitorinfo);
            if (bool.booleanValue()) {
                screenRectangles.add(monitorinfo.rcMonitor);
            }
            return 1;
        }, new WinDef.LPARAM(0));

        return screenRectangles;
    }
}
