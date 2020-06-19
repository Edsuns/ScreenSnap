package com.s0n1.screensnap.widget;

import javax.swing.*;
import java.awt.*;

import static com.s0n1.screensnap.ui.UiRes.COLOR_CAPTURE_DARK;
import static com.s0n1.screensnap.ui.UiRes.COLOR_CAPTURE_LIGHT;

/**
 * Created by Edsuns@qq.com on 2020-05-31
 */
public class ShotImageLabel extends JLabel {
    private int lineX = -1, lineY = -1;
    private int x, y, w, h;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (w < 1 || h < 1) return;
        // 选区内框
        g.setColor(COLOR_CAPTURE_LIGHT);
        g.fillRect(x - 1, y - 1, 1, h + 2);
        g.fillRect(x + w, y - 1, 1, h + 2);
        g.fillRect(x, y - 1, w, 1);
        g.fillRect(x, y + h, w, 1);
        // 选区外框
        g.setColor(COLOR_CAPTURE_DARK);
        g.fillRect(x - 2, y - 2, 1, h + 4);
        g.fillRect(x + w + 1, y - 2, 1, h + 4);
        g.fillRect(x - 1, y - 2, w + 2, 1);
        g.fillRect(x - 1, y + h + 1, w + 2, 1);

        // 右下角文字背景
        String text = w + " x " + h;
        FontMetrics metrics = getFontMetrics(g.getFont());
        int textW = metrics.stringWidth(text) + 8;
        int textH = metrics.getHeight() + 6;
        g.setColor(COLOR_CAPTURE_DARK);
        g.fillRect(x + w - textW, y + h - textH, textW, textH);
        // 右下角文字
        g.setColor(COLOR_CAPTURE_LIGHT);
        g.drawString(text, x + w - textW + 4, y + h - textH / 2 + 5);

        // 十字瞄准线
        if (lineX != -1 || lineY != -1) {
            g.drawLine(lineX, 0, lineX, getHeight());
            g.drawLine(0, lineY, getWidth(), lineY);
        }
    }

    /**
     * 画圈选区域
     *
     * @param x      x坐标
     * @param y      y坐标
     * @param width  宽
     * @param height 高
     */
    public void drawRectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        h = height;
        w = width;
        repaint();
    }

    /**
     * 画十字瞄准线
     *
     * @param x x坐标
     * @param y y坐标
     */
    public void drawCross(int x, int y) {
        lineX = x;
        lineY = y;
        repaint();
    }

    public void reset() {
        lineX = -1;
        lineY = -1;
        x = 0;
        y = 0;
        w = 0;
        h = 0;
        repaint();
    }
}
