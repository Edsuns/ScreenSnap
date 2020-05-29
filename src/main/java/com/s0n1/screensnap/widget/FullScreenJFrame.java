package com.s0n1.screensnap.widget;

import javax.swing.*;

/**
 * Created by Edsuns@qq.com on 2020-05-25
 */
public class FullScreenJFrame extends JFrame {

    public FullScreenJFrame() {
        setResizable(false);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // 不显示任务栏图标
        setType(Type.UTILITY);
    }
}
