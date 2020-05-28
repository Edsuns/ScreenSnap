package com.s0n1.capturer.ui;

import com.s0n1.capturer.util.DeviceUtil;
import com.s0n1.capturer.widget.FullScreenJFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Created by Edsuns@qq.com on 2020-05-26
 */
public class ShotJFrame extends FullScreenJFrame {
    private Robot robot;
    private ColorPanel colorPanel;
    private JLabel shotLabel;

    public ShotJFrame() {
        // 初始化robot
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        colorPanel = new ColorPanel();
        add(colorPanel);

        // 初始化显示截图的Label
        shotLabel = new JLabel();
        add(shotLabel);

        // 添加事件监听器
        addMouseListener(new MouseAdapter() {
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
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                refreshColorPanel(e.getX(), e.getY());
            }
        });
    }

    public void refreshColorPanel(final int mouseX, final int mouseY) {
        int panelX = mouseX - ColorPanel.Width - ColorPanel.Margin;
        int panelY = mouseY - ColorPanel.Height - ColorPanel.Margin;
        int position = getPositionInScreen(mouseX, mouseY);
        switch (position) {
            case LEFT & TOP:
                panelX = mouseX + ColorPanel.Margin;
                panelY = mouseY + ColorPanel.Margin;
                break;
            case TOP:
            case RIGHT & TOP:
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
        colorPanel.setBackground(robot.getPixelColor(mouseX, mouseY));
    }

    public void pickColor() {
        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        int x = mousePoint.x;
        int y = mousePoint.y;
        if (mPickColorListener != null) {
            mPickColorListener.onColorPicked(robot.getPixelColor(x, y));
        }
        setVisible(false);
        System.out.println("Pick >> x: " + x + ", y: " + y);
    }

    private int screenWidth;
    private int screenHeight;

    /**
     * 显示Frame和截图
     */
    public void startShot() {
        if (DeviceUtil.isOldVersionJava) {
            DisplayMode displayMode = DeviceUtil.getDisplay();
            screenWidth = displayMode.getWidth();
            screenHeight = displayMode.getHeight();
        } else {
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            screenWidth = dimension.width;
            screenHeight = dimension.height;
        }

        Image shot = robot.createScreenCapture(new Rectangle(screenWidth, screenHeight));
        shotLabel.setIcon(new ImageIcon(shot));
        setVisible(true);
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
        int leftOrRight = 0b1111;
        int topOrBottom = 0b1111;
        if (x < ColorPanel.Width + ColorPanel.Margin) {// Left
            leftOrRight = LEFT;
        } else if (x > screenWidth - ColorPanel.Width - ColorPanel.Margin) {// Right
            leftOrRight = RIGHT;
        }
        if (y < ColorPanel.Height + ColorPanel.Margin) {// Top
            topOrBottom = TOP;
        } else if (y > screenHeight - ColorPanel.Height - ColorPanel.Margin) {// Bottom
            topOrBottom = BOTTOM;
        }
        return leftOrRight & topOrBottom;
    }

    private PickColorListener mPickColorListener;

    public void setPickColorListener(PickColorListener l) {
        mPickColorListener = l;
    }

    public interface PickColorListener {
        void onColorPicked(Color color);
    }
}
