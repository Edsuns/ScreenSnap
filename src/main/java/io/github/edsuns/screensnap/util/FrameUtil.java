package io.github.edsuns.screensnap.util;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

/**
 * Created by Edsuns@qq.com on 2020-05-25
 */
public final class FrameUtil {
    public static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    private static final String JAVA_VERSION = System.getProperty("java.version");
    public static boolean isOldVersionJava = false;

    static {
        int index = JAVA_VERSION.indexOf(".");
        if (index != -1 && JAVA_VERSION.length() > ++index) {
            isOldVersionJava = Float.parseFloat(JAVA_VERSION.substring(0, ++index)) < 1.9f;
        }
    }

    // 系统设置的显示缩放(DPI缩放)比例
    public static final float DPI_SCALE = Toolkit.getDefaultToolkit().getScreenResolution() / 96f;

    public static int getScreenWidth() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDisplayMode().getWidth();
    }

    public static int getScreenHeight() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDisplayMode().getHeight();
    }

    /**
     * 在屏幕中间显示窗口
     */
    public static void setCenterLocation(Window window) {
        window.setLocationRelativeTo(null);
    }

    /**
     * 设置为系统UI样式并缩放字体大小
     */
    public static void setSystemLookAndFeel() {
        // 设置系统默认样式
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        // 获取系统字体
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] allFonts = environment.getAvailableFontFamilyNames();
        String fontName = null;
        for (String font : allFonts) {
            if ("Microsoft YaHei UI".equals(font)
                    || "PingFang SC".equals(font) || "YaHei Consolas Hybrid".equals(font)) {
                fontName = font;
                break;
            }
        }
        // 设置为系统字体并缩放字体大小，要在设置样式后
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                FontUIResource resource = (FontUIResource) value;
                int style = resource.getStyle();
                // 使用JVM参数关闭DPI缩放在低版本Java上字体仍然会被缩放
                // 由于本程序已强制要求关闭DPI缩放，高版本Java也带上字体缩放便可获得一致的显示效果
                float size = FrameUtil.isOldVersionJava ?
                        resource.getSize() : resource.getSize() * DPI_SCALE;
                UIManager.put(key, new FontUIResource(fontName, style, (int) size));
            }
        }
    }
}
