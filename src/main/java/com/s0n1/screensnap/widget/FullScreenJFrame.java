package com.s0n1.screensnap.widget;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Edsuns@qq.com on 2020-05-25
 */
public class FullScreenJFrame extends JFrame {

    public FullScreenJFrame() {
        setResizable(false);
        setUndecorated(true);
        setAlwaysOnTop(true);
        // 不显示任务栏图标
        setType(Type.UTILITY);
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            // 必须在setVisible这里执行setFullScreenWindow和setExtendedState
            // 兼容Linux
            GraphicsEnvironment localGraphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
            localGraphics.getDefaultScreenDevice().setFullScreenWindow(this);
            // 兼容Windows
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        super.setVisible(b);
    }
}
