package io.github.edsuns.screensnap.ui;

import io.github.edsuns.screensnap.tools.Settings;
import io.github.edsuns.screensnap.widget.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

/**
 * 热键设置对话框
 * Created by Edsuns@qq.com on 2020-05-30
 */
public class HotkeyDialog extends JDialog {
    private final JTextField hotkeyText;
    public static final List<Integer> MODIFIERS = Arrays.asList(KeyEvent.VK_ALT, KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_META);

    public HotkeyDialog(JFrame parent) {
        super(parent, true);
        getContentPane().setPreferredSize(new Dimension(300, 125));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        final int marginTop = 30;
        final int marginLeft = 30;

        // 内容 宽240
        hotkeyText = new JTextField();
        hotkeyText.setBounds(marginLeft, marginTop, 140, 30);
        hotkeyText.setHorizontalAlignment(SwingConstants.CENTER);
        hotkeyText.setEditable(false);
        hotkeyText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                parseKey(e);
            }
        });
        add(hotkeyText);

        JButton applyBtn = new JButton(Application.res().getString("apply"));
        applyBtn.setFocusable(false);
        applyBtn.setBounds(marginLeft + 160, marginTop, 80, 30);
        applyBtn.addActionListener(e -> {
            String hotkey = hotkeyText.getText();
            if (hotkey == null || hotkey.isEmpty()) return;

            if (mHotkeyChangeListener != null) {
                mHotkeyChangeListener.onHotkeyChange(hotkey);
            }
            Settings.setHotkey(hotkey);
            dispose();
        });
        add(applyBtn);

        JLabel tipsLabel = new JLabel();
        tipsLabel.setFocusable(false);
        tipsLabel.setBounds(marginLeft, marginTop + 30 + 20, 240, 30);
        tipsLabel.setText(Application.res().getString("hotkey_tips"));
        tipsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(tipsLabel);

        pack();
    }

    private void parseKey(KeyEvent event) {
        String newHotKey = KeyStroke.getKeyStrokeForEvent(event).toString();
        // 如果超过两个键（注意pressed的空格要在前面）
        boolean isMultiKey = !MODIFIERS.contains(event.getKeyCode()) && newHotKey.contains(" pressed");
        if (isMultiKey) {
            hotkeyText.setText(newHotKey.replaceAll(" pressed", ""));
        }
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {// 必须在super前
            hotkeyText.setText(Settings.getHotkey());
        }
        super.setVisible(b);
    }

    HotkeyChangeListener mHotkeyChangeListener;

    public interface HotkeyChangeListener {
        void onHotkeyChange(String newHotkey);
    }

    public void setHotkeyChangeListener(HotkeyChangeListener listener) {
        mHotkeyChangeListener = listener;
    }
}
