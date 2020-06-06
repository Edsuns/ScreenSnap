package com.s0n1.screensnap.ui;

import com.s0n1.screensnap.tools.AnimationInvoker;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import static com.s0n1.screensnap.ui.UiRes.COLOR_GRAY_LIGHT;
import static com.s0n1.screensnap.util.DeviceUtil.SCREEN_HEIGHT;
import static com.s0n1.screensnap.util.DeviceUtil.SCREEN_WIDTH;

/**
 * Created by Edsuns@qq.com on 2020-06-06
 */
public class Toast extends JFrame {
    private static Toast instance;
    private final JLabel msgLabel;
    public static final int DELAY_DEFAULT = 2000;

    private Toast() {
        final int width = 300;
        final int height = 80;
        setBounds((SCREEN_WIDTH - width) / 2, (SCREEN_HEIGHT - height) / 2, width, height);
        setType(Type.UTILITY);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setUndecorated(true);
        setAutoRequestFocus(false);
        setFocusable(false);
        setBackground(new Color(0, true));

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(0.95f, 0.95f, 0.97f, 0.95f));
        contentPanel.setLayout(null);
        contentPanel.setBorder(BorderFactory.createLineBorder(COLOR_GRAY_LIGHT));
        setContentPane(contentPanel);

        msgLabel = new JLabel();
        msgLabel.setBounds(0, 0, width, height);
        msgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        msgLabel.setFont(msgLabel.getFont().deriveFont(Font.BOLD));
        contentPanel.add(msgLabel);
    }

    public static Toast getInstance() {
        if (instance == null) {
            instance = new Toast();
        }
        return instance;
    }

    private Timer timer;

    public void show(String msg, int delay) {
        if (timer != null) {
            timer.cancel();
        }
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
