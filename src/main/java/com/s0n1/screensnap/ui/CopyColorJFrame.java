package com.s0n1.screensnap.ui;

import com.s0n1.screensnap.util.AppUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.s0n1.screensnap.ui.UiRes.COPY_ICON;
import static com.s0n1.screensnap.util.DeviceUtil.SCREEN_HEIGHT;
import static com.s0n1.screensnap.util.DeviceUtil.SCREEN_WIDTH;

public class CopyColorJFrame extends JFrame {
    private final JLabel colorLabel;
    private final JTextField hexColorText;
    private final JTextField rgbColorText;
    private final JTextField htmlColorText;

    public CopyColorJFrame() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds((SCREEN_WIDTH - 360) / 2, (SCREEN_HEIGHT - 160) / 2, 330, 160);
        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);

        colorLabel = new JLabel();
        colorLabel.setBounds(10, 15, 100, 100);
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
        contentPane.add(colorLabel);

        hexColorText = new JTextField();
        hexColorText.setEditable(false);
        hexColorText.setBounds(165, 15, 110, 24);
        contentPane.add(hexColorText);

        rgbColorText = new JTextField();
        rgbColorText.setEditable(false);
        rgbColorText.setBounds(165, 51, 110, 24);
        contentPane.add(rgbColorText);

        htmlColorText = new JTextField();
        htmlColorText.setEditable(false);
        htmlColorText.setBounds(165, 89, 110, 24);
        contentPane.add(htmlColorText);

        JLabel hexLabel = new JLabel("HEX");
        hexLabel.setBounds(125, 18, 40, 18);
        contentPane.add(hexLabel);

        JLabel rgbLabel = new JLabel("RGB");
        rgbLabel.setBounds(125, 52, 40, 18);
        contentPane.add(rgbLabel);

        JLabel htmlLabel = new JLabel("HTML");
        htmlLabel.setBounds(125, 90, 40, 18);
        contentPane.add(htmlLabel);

        int size = 25;
        ImageIcon copyIcon = new ImageIcon(COPY_ICON.getScaledInstance(size, size, Image.SCALE_DEFAULT));

        JButton copyHexBtn = new JButton();
        copyHexBtn.setBounds(285, 15, size, size);
        copyHexBtn.setIcon(copyIcon);
        copyHexBtn.addActionListener(e -> AppUtil.copy(hexColorText.getText()));
        contentPane.add(copyHexBtn);

        JButton copyRgbBtn = new JButton();
        copyRgbBtn.setBounds(285, 51, size, size);
        copyRgbBtn.setIcon(copyIcon);
        copyRgbBtn.addActionListener(e -> AppUtil.copy(rgbColorText.getText()));
        contentPane.add(copyRgbBtn);

        JButton copyHtmlBtn = new JButton();
        copyHtmlBtn.setBounds(285, 89, size, size);
        copyHtmlBtn.setIcon(copyIcon);
        copyHtmlBtn.addActionListener(e -> AppUtil.copy(htmlColorText.getText()));
        contentPane.add(copyHtmlBtn);

    }

    public void showCopy(Color color) {
        colorLabel.setBackground(color);
        hexColorText.setText(AppUtil.getColorText(color, AppUtil.ColorMode.HEX));
        rgbColorText.setText(AppUtil.getColorText(color, AppUtil.ColorMode.RGB));
        htmlColorText.setText(AppUtil.getColorText(color, AppUtil.ColorMode.HTML));
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
