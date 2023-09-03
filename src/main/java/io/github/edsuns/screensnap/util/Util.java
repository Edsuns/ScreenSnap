package io.github.edsuns.screensnap.util;

import io.github.edsuns.screensnap.widget.ImageTransferable;
import sun.awt.ComponentFactory;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.*;
import java.awt.peer.RobotPeer;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Edsuns@qq.com on 2020-05-26
 */
public final class Util {

    private static final GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();

    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();

    private static final RobotPeer robotPeer;

    static {
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        final MethodType methodType;
        try {
            if (Runtime.version().feature() < 15) {
                methodType = MethodType.methodType(RobotPeer.class, Robot.class, GraphicsDevice.class);
                MethodHandle methodHandle = lookup.findVirtual(ComponentFactory.class, "createRobot", methodType).bindTo(toolkit);
                robotPeer = (RobotPeer) methodHandle.invokeExact((Robot) null, localGraphicsEnvironment.getDefaultScreenDevice());
            } else {
                methodType = MethodType.methodType(RobotPeer.class, GraphicsDevice.class);
                MethodHandle methodHandle = lookup.findVirtual(ComponentFactory.class, "createRobot", methodType).bindTo(toolkit);
                robotPeer = (RobotPeer) methodHandle.invokeExact(localGraphicsEnvironment.getDefaultScreenDevice());
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage multiDisplayScreenshot() {
        Rectangle fullVirtualScreenRectangle = getFullVirtualScreenRectangle();
        return screenshot(fullVirtualScreenRectangle);
    }

    public static BufferedImage screenshot(Rectangle rect){

        int[] pixels = robotPeer.getRGBPixels(rect);
        DirectColorModel screenCapCM = new DirectColorModel(24,
                /* red mask */ 0x00FF0000,
                /* green mask */ 0x0000FF00,
                /* blue mask */ 0x000000FF);
        DataBufferInt buffer = new DataBufferInt(pixels, pixels.length);
        int[] bandmasks = new int[3];
        bandmasks[0] = screenCapCM.getRedMask();
        bandmasks[1] = screenCapCM.getGreenMask();
        bandmasks[2] = screenCapCM.getBlueMask();

        WritableRaster raster = Raster.createPackedRaster(buffer, rect.width,
                rect.height, rect.width, bandmasks, null);
        BufferedImage highResolutionImage = new BufferedImage(screenCapCM, raster, false, null);
        return highResolutionImage;
    }

    public static void setFullScreenWindow(Window window) {
        Rectangle fullRect = getFullVirtualScreenRectangle();
        window.setLocation(new Point(fullRect.x, fullRect.y));
        window.setSize(new Dimension(fullRect.width, fullRect.height));
    }

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
        toolkit.getSystemClipboard().setContents(trans, null);
    }

    public static void copyImage(Image image) {
        Transferable trans = new ImageTransferable(image);
        toolkit.getSystemClipboard().setContents(trans, null);
    }

    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        return dateFormat.format(new Date());
    }


    public static Color getColorFromScreenPixel(int x, int y) {
       return new Color(robotPeer.getRGBPixel(x, y));
    }


    public static List<Rectangle> getAllScreenRectangles() {
        GraphicsDevice[] screenDevices = localGraphicsEnvironment.getScreenDevices();
        List<Rectangle> allBounds = new ArrayList<>();
        for (int i = 0; i < screenDevices.length; i++) {
            GraphicsDevice screenDevice = screenDevices[i];
            GraphicsConfiguration gc = screenDevice.getDefaultConfiguration();
            // 获取 GraphicsConfiguration 的 Bounds，它包含更高分辨率信息
            Rectangle screenBounds = gc.getBounds();
            if (System.getProperty("os.name").toLowerCase(Locale.ENGLISH).startsWith("windows")) {
                screenBounds.width = screenDevice.getDisplayMode().getWidth();
                screenBounds.height = screenDevice.getDisplayMode().getHeight();
            }
            allBounds.add(screenBounds);
        }
        return allBounds;
    }


    public static Rectangle getFullVirtualScreenRectangle(){
        return getAllScreenRectangles().stream().reduce(Rectangle::union).get();
    }

    public static void mouseMove(int x,int y){
        robotPeer.mouseMove(x,y);
    }
}
