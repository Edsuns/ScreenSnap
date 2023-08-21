package io.github.edsuns.screensnap.ui;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import io.github.edsuns.screensnap.util.Util;
import io.github.edsuns.screensnap.widget.FullScreenJFrame;
import io.github.edsuns.screensnap.widget.ShotImageLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * 全屏的屏幕取样界面
 * Created by Edsuns@qq.com on 2020-05-26
 */
public class ShotJFrame extends FullScreenJFrame {
    private final ColorPanel colorPanel;
    private final ShotImageLabel shotLabel;

    private BufferedImage shotImage;
    private int xStart, yStart, xEnd, yEnd;// 鼠标起始及结束位置
    private int recX, recY, recH, recW;// 图像选区
    boolean pressed;

    public ShotJFrame() {

        // 添加按键监听
        initKeyboardListener();

        colorPanel = new ColorPanel();
        add(colorPanel);

        // 初始化显示截图的 Label
        shotLabel = new ShotImageLabel();
        add(shotLabel);

        // 添加事件监听器
        shotLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int buttonKey = e.getButton();
                if (buttonKey == MouseEvent.BUTTON1) {
                    System.out.println("Left clicked");
                    pickColor();
                } else if (buttonKey == MouseEvent.BUTTON3) {
                    System.out.println("Right clicked");
                    stopShot();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Pressed");
                // 初始化选区功能
                pressed = true;
                colorPanel.setVisible(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                xStart = e.getX();
                yStart = e.getY();
                recW = 0;
                recH = 0;
//                shotLabel.drawCross(xStart, yStart);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // 重置选区功能
                pressed = false;
                colorPanel.setVisible(true);
                shotLabel.reset();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                if (recW < 1 || recH < 1 || mPickColorListener == null) return;
                int buttonKey = e.getButton();
                if (buttonKey == MouseEvent.BUTTON1) {
                    System.out.println("Left Released");
                    mPickColorListener.onLeftCapture(shotImage.getSubimage(recX, recY, recW, recH));
                    stopShot();
                } else if (buttonKey == MouseEvent.BUTTON3) {
                    System.out.println("Right Released");
                    mPickColorListener.onRightCapture(shotImage.getSubimage(recX, recY, recW, recH));
                    stopShot();
                }
            }
        });
        shotLabel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (pressed) {
                    xEnd = e.getX();
                    yEnd = e.getY();
                    int maxX = Math.max(xStart, xEnd);
                    int maxY = Math.max(yStart, yEnd);
                    int minX = Math.min(xStart, xEnd);
                    int minY = Math.min(yStart, yEnd);
                    recX = minX;
                    recY = minY;
                    recW = Math.max(1, maxX - minX + 1);
                    recH = Math.max(1, maxY - minY + 1);
                    shotLabel.drawRectangle(recX, recY, recW, recH);
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
//                if (pressed) {
//                    shotLabel.drawCross(e.getX(), e.getY());
//                } else {
//                    refreshColorPanel(e.getX(), e.getY());
//                }
                refreshColorPanel(e.getX(), e.getY());
            }
        });
    }

    /**
     * 初始化键盘监听事件
     */
    private void initKeyboardListener() {
        AWTEventListener listener = event -> {
            // 过滤出 KeyEvent
            if (event.getClass() != KeyEvent.class) return;
            KeyEvent key = (KeyEvent) event;

            if (key.getID() == KeyEvent.KEY_PRESSED) {
                Point mousePoint = MouseInfo.getPointerInfo().getLocation();
                int x = mousePoint.x;
                int y = mousePoint.y;
                switch (key.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        // robot.mouseMove(x, y - 1);
                        moveCursor(0, -1);
                        break;
                    case KeyEvent.VK_DOWN:
                        // robot.mouseMove(x, y + 1);
                        moveCursor(0, 1);
                        break;
                    case KeyEvent.VK_LEFT:
                        // robot.mouseMove(x - 1, y);
                        moveCursor(-1, 0);
                        break;
                    case KeyEvent.VK_RIGHT:
                        // robot.mouseMove(x + 1, y);
                        moveCursor(1, 0);
                        break;
                    case KeyEvent.VK_ENTER:
                    case KeyEvent.VK_SPACE:
                        pickColor();
                        break;
                }
            }
        };
        Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.KEY_EVENT_MASK);
    }

    private static void moveCursor(int dx, int dy) {
        User32 user32 = User32.INSTANCE;
        WinDef.POINT point = new WinDef.POINT();
        user32.GetCursorPos(point);
        int newX = point.x + dx;
        int newY = point.y + dy;
        user32.SetCursorPos(newX, newY);
    }

    public void refreshColorPanel(final int mouseX, final int mouseY) {

        int panelX = mouseX - ColorPanel.Width;
        int panelY = mouseY - ColorPanel.Height - ColorPanel.Margin;
        int position = getPositionInScreen(mouseX, mouseY);
        switch (position) {
            case LEFT & TOP:
                panelX = mouseX + ColorPanel.Margin;
                panelY = mouseY + ColorPanel.Margin;
                break;
            case TOP:
            case RIGHT & TOP:
                panelX = mouseX - ColorPanel.Width - ColorPanel.Margin;
                panelY = mouseY + ColorPanel.Margin;
                break;
            case LEFT:
            case LEFT & BOTTOM:
                panelX = mouseX + ColorPanel.Margin;
                break;
            case RIGHT:
            case BOTTOM:
            case RIGHT & BOTTOM:
            default:
                break;
        }
        colorPanel.setLocation(panelX, panelY);
        colorPanel.updateColor(mouseX, mouseY);
    }

    private void pickColor() {
        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        int x = mousePoint.x;
        int y = mousePoint.y;
        if (mPickColorListener != null) {
            Color color = Util.getColorFromScreenPixel(x, y);
            mPickColorListener.onColorPicked(color);
            System.out.println("Picked x:" + x + ",y:" + y);
        }
        setVisible(false);
        System.out.println("Point x: " + x + ", y: " + y);
    }


    /**
     * 显示 Frame 和截图
     */
    public void startShot() {
        if (isVisible()) return;
        // Windows 自带的 Console 能在 print 处暂停程序运行, 导致界面卡住. 提前 print, 要暂停也是在全屏前暂停
        System.out.println("Start Screenshot.");

        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        refreshColorPanel(mousePoint.x, mousePoint.y);

        // shotImage = robot.createScreenCapture(
        //         new Rectangle(FrameUtil.getScreenWidth(), FrameUtil.getScreenHeight()));
        CustomGDI32Util customGDI32Util = new CustomGDI32Util(getFullVirtualScreenRect());
        shotImage = customGDI32Util.getScreenshot();
        // shotImage = robot.createScreenCapture(new Rectangle( -1920,0,1920,1080));
        shotLabel.setIcon(new ImageIcon(shotImage));
        setVisible(true);
    }


    public static Rectangle getFullVirtualScreenRect() {
        int x = User32.INSTANCE.GetSystemMetrics(WinUser.SM_XVIRTUALSCREEN);
        int y = User32.INSTANCE.GetSystemMetrics(WinUser.SM_YVIRTUALSCREEN);
        int w = User32.INSTANCE.GetSystemMetrics(WinUser.SM_CXVIRTUALSCREEN);
        int h = User32.INSTANCE.GetSystemMetrics(WinUser.SM_CYVIRTUALSCREEN);
        Rectangle screenRect = new Rectangle(x, y, w, h);
        return screenRect;
    }

    public void stopShot() {
        shotLabel.setIcon(null);
        setVisible(false);
    }

    private static final int LEFT = 0b0111;
    private static final int RIGHT = 0b1011;
    private static final int TOP = 0b1101;
    private static final int BOTTOM = 0b1110;

    private int getPositionInScreen(final int x, final int y) {
        int leftOrRight = x < ColorPanel.Width ? LEFT : RIGHT;
        int topOrBottom = y < ColorPanel.Height + ColorPanel.Margin ? TOP : BOTTOM;
        return leftOrRight & topOrBottom;
    }

    private PickColorListener mPickColorListener;

    public void setPickColorListener(PickColorListener l) {
        mPickColorListener = l;
    }

    public interface PickColorListener {
        void onColorPicked(Color color);

        void onRightCapture(BufferedImage image);

        void onLeftCapture(BufferedImage image);
    }
}
