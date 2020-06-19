package com.s0n1.screensnap.ui;

import com.s0n1.screensnap.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.s0n1.screensnap.ui.UiRes.COPY_ICON;

/**
 * Created by Edsuns@qq.com on 2020-06-02
 */
public class CopyColorJFrame extends JFrame {
    private final JLabel colorLabel;
    private final JTextField hexColorText;
    private final JTextField rgbColorText;
    private final JTextField htmlColorText;

    public CopyColorJFrame() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        getContentPane().setPreferredSize(new Dimension(320, 125));
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

        hexColorText = new JTextField();
        hexColorText.setEditable(false);
        hexColorText.setBounds(165, 15, 110, 24);
        add(hexColorText);

        rgbColorText = new JTextField();
        rgbColorText.setEditable(false);
        rgbColorText.setBounds(165, 51, 110, 24);
        add(rgbColorText);

        htmlColorText = new JTextField();
        htmlColorText.setEditable(false);
        htmlColorText.setBounds(165, 89, 110, 24);
        add(htmlColorText);

        JLabel hsbLabel = new JLabel("HSB");
        hsbLabel.setBounds(125, 18, 40, 18);
        add(hsbLabel);

        JLabel rgbLabel = new JLabel("RGB");
        rgbLabel.setBounds(125, 52, 40, 18);
        add(rgbLabel);

        JLabel htmlLabel = new JLabel("HTML");
        htmlLabel.setBounds(125, 90, 40, 18);
        add(htmlLabel);

        final int size = 25;
        ImageIcon copyIcon = new ImageIcon(COPY_ICON.getScaledInstance(size, size, Image.SCALE_SMOOTH));

        JButton copyHsbBtn = new JButton();
        copyHsbBtn.setBounds(285, 15, size, size);
        copyHsbBtn.setIcon(copyIcon);
        copyHsbBtn.addActionListener(e -> Util.copyText(hexColorText.getText()));
        add(copyHsbBtn);

        JButton copyRgbBtn = new JButton();
        copyRgbBtn.setBounds(285, 51, size, size);
        copyRgbBtn.setIcon(copyIcon);
        copyRgbBtn.addActionListener(e -> Util.copyText(rgbColorText.getText()));
        add(copyRgbBtn);

        JButton copyHtmlBtn = new JButton();
        copyHtmlBtn.setBounds(285, 89, size, size);
        copyHtmlBtn.setIcon(copyIcon);
        copyHtmlBtn.addActionListener(e -> Util.copyText(htmlColorText.getText()));
        add(copyHtmlBtn);

        pack();
    }

    public void showCopy(Color color) {
        colorLabel.setBackground(color);
        hexColorText.setText(Util.getColorText(color, Util.ColorMode.HSB));
        rgbColorText.setText(Util.getColorText(color, Util.ColorMode.RGB));
        htmlColorText.setText(Util.getColorText(color, Util.ColorMode.HTML));
        Util.setCenterLocation(this);
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
