package com.s0n1.screensnap.widget;

import com.s0n1.screensnap.tools.AnimationInvoker;
import com.s0n1.screensnap.util.FrameUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import static com.s0n1.screensnap.ui.UiRes.COLOR_GRAY_LIGHT;

/**
 * Created by Edsuns@qq.com on 2020-06-06
 */
public class Toast extends JFrame {
    private static Toast instance;
    private final JLabel msgLabel;
    public static final int DELAY_DEFAULT = 2000;

    private Toast() {
        setType(Type.UTILITY);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setUndecorated(true);
        setAutoRequestFocus(false);
        setFocusable(false);
        setBackground(new Color(0, true));

        final int width = 200;
        final int height = 60;
        JPanel contentPanel = new JPanel();
        contentPanel.setPreferredSize(new Dimension(width, height));
        contentPanel.setBackground(new Color(0.95f, 0.95f, 0.97f, 0.95f));
        contentPanel.setBorder(BorderFactory.createLineBorder(COLOR_GRAY_LIGHT));
        contentPanel.setLayout(null);
        setContentPane(contentPanel);

        msgLabel = new JLabel();
        msgLabel.setBounds(0, 0, width, height);
        msgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        Font font = msgLabel.getFont();
        msgLabel.setFont(font.deriveFont(Font.BOLD, font.getSize() * 1.2f));
        contentPanel.add(msgLabel);

        pack();
    }

    public static Toast getInstance() {
        if (instance == null) {
            instance = new Toast();
            FrameUtil.setCenterLocation(instance);
        }
        return instance;
    }

    private Timer timer;

    public void show(String msg, int delay) {
        if (timer != null) {
            timer.cancel();
        }
        // cancel()后就不能schedule()，重新实例化一个
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                setVisible(false);
            }
        }, delay);
        msgLabel.setText(msg);
        AnimationInvoker.show(this);
    }
}
