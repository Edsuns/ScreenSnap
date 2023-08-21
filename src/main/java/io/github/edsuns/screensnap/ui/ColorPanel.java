package io.github.edsuns.screensnap.ui;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser;
import io.github.edsuns.screensnap.util.Util;

import javax.swing.*;
import java.awt.*;

/**
 * 悬浮窗，包含放大镜
 * Created by Edsuns@qq.com on 2020-06-03
 */
public class ColorPanel extends JPanel {
    public static final int Width = 235;
    public static final int Height = 142;
    public static final int Margin = 50;

    private final JLabel colorLabel;
    private final JLabel pointLabel;
    private final JLabel colorTextLabel;

    private Image pointImage;

    public ColorPanel() {
        setSize(Width, Height);
        setLayout(null);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // 颜色块标签
        colorLabel = new JLabel();
        colorLabel.setBounds(120, 5, 110, 110);
        colorLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        colorLabel.setOpaque(true);
        add(colorLabel);

        // 颜色坐标标签
        pointLabel = new JLabel();
        pointLabel.setBounds(10, 120, 100, 18);
        pointLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(pointLabel);

        // 颜色值标签
        colorTextLabel = new JLabel();
        colorTextLabel.setBounds(120, 120, 105, 18);
        colorTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(colorTextLabel);
    }

    public void updateColor(int x, int y) {
        int vX = User32.INSTANCE.GetSystemMetrics(WinUser.SM_XVIRTUALSCREEN);
        int vY = User32.INSTANCE.GetSystemMetrics(WinUser.SM_YVIRTUALSCREEN);
        int pX = x + vX;
        int pY = y + vY;

        Color color = Util.getColorFromScreenPixel(pX, pY);
        pointLabel.setText("(" + pX + "," + pY + ")");
        colorLabel.setBackground(color);
        colorTextLabel.setText(Util.getColorText(color, Util.ColorMode.HTML));
        // 截取鼠标区域图像并绘制放大镜
        // pointImage = robot.createScreenCapture(new Rectangle(x - 5, y - 5, 11, 11));
        CustomGDI32Util customGDI32Util = new CustomGDI32Util(new Rectangle(pX - 5, pY - 5, 11, 11));
        pointImage = customGDI32Util.getScreenshot();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 放大镜
        g.drawImage(pointImage, 5, 5, 110, 110, null);
        // 放大镜边框
        g.setColor(Color.GRAY);
        g.fillRect(5, 5, 110, 1);
        g.fillRect(5, 114, 110, 1);
        g.fillRect(5, 5, 1, 110);
        g.fillRect(114, 5, 1, 110);

        // 瞄准外方框
        g.fillRect(55, 55, 10, 1);
        g.fillRect(55, 55, 1, 11);
        g.fillRect(55, 65, 10, 1);
        g.fillRect(65, 55, 1, 11);
        // 瞄准内方框
        g.setColor(UiRes.COLOR_CAPTURE_LIGHT);
        g.fillRect(56, 56, 8, 1);
        g.fillRect(56, 56, 1, 9);
        g.fillRect(56, 64, 8, 1);
        g.fillRect(64, 56, 1, 9);
    }
}
