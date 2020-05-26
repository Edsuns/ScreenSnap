package com.s0n1.capturer.ui;

import com.s0n1.capturer.util.ScreenUtil;
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
    private JLabel shotLabel;

    public ShotJFrame() {
        // 初始化robot
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

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
                    setVisible(false);
                    System.exit(0);
                } else if (buttonKey == MouseEvent.BUTTON3) {
                    System.out.println("Right clicked");
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                refreshColorWindow(e.getX(), e.getY());
            }
        });
    }

    public void refreshColorWindow(final int x, final int y) {
        System.out.println("refresh | x:"+ x + ", y:"+y);
    }

    public void pickColor() {
        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        int x = mousePoint.x;
        int y = mousePoint.y;
        if (mPickColorListener != null) {
            mPickColorListener.onColorPicked(robot.getPixelColor(x, y));
        }
        System.out.println(robot.getPixelColor(x, y) + " x:"+ x + ", y:"+y);
    }

    /**
     * 显示Frame和截图
     */
    public void showShot() {
        DisplayMode displayMode = ScreenUtil.getDisplay();
        int screenWidth = displayMode.getWidth();
        int screenHeight = displayMode.getHeight();

        Image shot = robot.createScreenCapture(new Rectangle(screenWidth, screenHeight));
        shotLabel.setIcon(new ImageIcon(shot));
        setVisible(true);
    }

    private PickColorListener mPickColorListener;

    public void setPickColorListener(PickColorListener l) {
        mPickColorListener = l;
    }

    public interface PickColorListener{
        void onColorPicked(Color color);
    }
}
