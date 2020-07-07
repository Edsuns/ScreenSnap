package com.s0n1.screensnap.ui;

import com.s0n1.screensnap.util.FrameUtil;
import com.s0n1.screensnap.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.s0n1.screensnap.ui.UiRes.COPY_ICON;

/**
 * 颜色复制窗口
 * Created by Edsuns@qq.com on 2020-06-02
 */
public class CopyColorJFrame extends JFrame {
    private final JLabel colorLabel;
    private final JTextField hsbColorText;
    private final JTextField rgbColorText;
    private final JTextField hexColorText;

    public CopyColorJFrame() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        getContentPane().setPreferredSize(new Dimension(340, 125));
        setLayout(null);

        colorLabel = new JLabel();
        colorLabel.setBounds(10, 15, 100, 100);
        colorLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        colorLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // 重要，设置背景颜色必须先将它设置为不透明的
        colorLabel.setOpaque(true);
        colorLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (mPickAnotherCallback != null) {
                        mPickAnotherCallback.onPickAnotherColor();
                    }
                }
            }
        });
        add(colorLabel);

        hsbColorText = new JTextField();
        hsbColorText.setEditable(false);
        hsbColorText.setBounds(165, 15, 130, 25);
        add(hsbColorText);

        rgbColorText = new JTextField();
        rgbColorText.setEditable(false);
        rgbColorText.setBounds(165, 52, 130, 25);
        add(rgbColorText);

        hexColorText = new JTextField();
        hexColorText.setEditable(false);
        hexColorText.setBounds(165, 90, 130, 25);
        add(hexColorText);

        JLabel hsbLabel = new JLabel("HSB");
        hsbLabel.setBounds(125, 19, 40, 18);
        add(hsbLabel);

        JLabel rgbLabel = new JLabel("RGB");
        rgbLabel.setBounds(125, 56, 40, 18);
        add(rgbLabel);

        JLabel hexLabel = new JLabel("HEX");
        hexLabel.setBounds(125, 93, 40, 18);
        add(hexLabel);

        final int size = 25;
        ImageIcon copyIcon = new ImageIcon(COPY_ICON.getScaledInstance(size, size, Image.SCALE_SMOOTH));

        JButton copyHsbBtn = new JButton();
        copyHsbBtn.setBounds(305, 15, size, size);
        copyHsbBtn.setIcon(copyIcon);
        copyHsbBtn.addActionListener(e -> Util.copyText(hsbColorText.getText()));
        add(copyHsbBtn);

        JButton copyRgbBtn = new JButton();
        copyRgbBtn.setBounds(305, 52, size, size);
        copyRgbBtn.setIcon(copyIcon);
        copyRgbBtn.addActionListener(e -> Util.copyText(rgbColorText.getText()));
        add(copyRgbBtn);

        JButton copyHexBtn = new JButton();
        copyHexBtn.setBounds(305, 90, size, size);
        copyHexBtn.setIcon(copyIcon);
        copyHexBtn.addActionListener(e -> Util.copyText(hexColorText.getText()));
        add(copyHexBtn);

        pack();
    }

    public void showCopy(Color color) {
        colorLabel.setBackground(color);
        hsbColorText.setText(Util.getColorText(color, Util.ColorMode.HSB));
        rgbColorText.setText(Util.getColorText(color, Util.ColorMode.RGB));
        hexColorText.setText(Util.getColorText(color, Util.ColorMode.HEX));
        FrameUtil.setCenterLocation(this);
        setExtendedState(JFrame.NORMAL);
        setVisible(true);
        toFront();
    }

    private PickAnotherCallback mPickAnotherCallback;

    public void setPickAnotherCallback(PickAnotherCallback callback) {
        mPickAnotherCallback = callback;
    }

    public interface PickAnotherCallback {
        void onPickAnotherColor();
    }
}
