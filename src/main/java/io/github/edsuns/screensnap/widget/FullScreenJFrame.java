package io.github.edsuns.screensnap.widget;

import io.github.edsuns.screensnap.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Edsuns@qq.com on 2020-05-25
 */
public class FullScreenJFrame extends JWindow {

    public FullScreenJFrame() {
        // setResizable(false);
        // setUndecorated(true);
        setAlwaysOnTop(true);
        // 不显示任务栏图标
        setType(Type.UTILITY);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                setVisible(false);// windowDeactivated时窗口会被非全屏化，不如直接不显示
            }
        });
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            // 必须在setVisible这里执行setFullScreenWindow和setExtendedState
            // 兼容Linux
            GraphicsEnvironment localGraphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
            localGraphics.getDefaultScreenDevice().setFullScreenWindow(this);
            Util.setFullScreenWindow(this);
            // 兼容Windows
            // setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        super.setVisible(b);
    }
}
