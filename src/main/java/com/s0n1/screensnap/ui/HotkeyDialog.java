package com.s0n1.screensnap.ui;

import com.s0n1.screensnap.tools.GlobalHotKey;
import com.s0n1.screensnap.tools.Settings;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

import static com.s0n1.screensnap.util.DeviceUtil.SCREEN_HEIGHT;
import static com.s0n1.screensnap.util.DeviceUtil.SCREEN_WIDTH;

public class HotkeyDialog extends JDialog {
    private final JTextField hotkeyText;
    public static final List<Integer> MODIFIERS = Arrays.asList(KeyEvent.VK_ALT, KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_META);

    public HotkeyDialog(JFrame parent) {
        super(parent, "Change Hotkey", true);
        setBounds((SCREEN_WIDTH - 300) / 2, (SCREEN_HEIGHT - 200) / 2, 300, 200);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(null);

        int marginTop = 30;
        int marginLeft = 20;

        // 内容 宽240
        hotkeyText = new JTextField();
        hotkeyText.setBounds(marginLeft, marginTop, 140, 30);
        hotkeyText.setEditable(false);
        hotkeyText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                parseKey(e);
            }
        });
        add(hotkeyText);

        JButton applyBtn = new JButton("Apply");
        applyBtn.setFocusable(false);
        applyBtn.setBounds(marginLeft + 160, marginTop, 80, 30);
        applyBtn.addActionListener(e -> {
            String hotkey = hotkeyText.getText();
            if (hotkey == null || hotkey.isEmpty()) return;

            if (mHotkeyChangeListener != null) {
                mHotkeyChangeListener.onHotkeyChange(hotkey);
            }
            Settings.setHotkey(hotkey);
            GlobalHotKey.getInstance().setupHotKey();
            dispose();
        });
        add(applyBtn);

        JLabel tipsLabel = new JLabel();
        tipsLabel.setFocusable(false);
        tipsLabel.setBounds(marginLeft, marginTop + 30 + 10, 240, 60);
        tipsLabel.setText("Type new global hotkey.");
        tipsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(tipsLabel);
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
