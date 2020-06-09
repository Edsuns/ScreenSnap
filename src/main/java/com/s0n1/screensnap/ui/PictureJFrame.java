package com.s0n1.screensnap.ui;

import com.s0n1.screensnap.util.DeviceUtil;
import com.s0n1.screensnap.util.Util;
import com.s0n1.screensnap.widget.PictureViewer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.s0n1.screensnap.ui.UiRes.*;

/**
 * Created by Edsuns@qq.com on 2020-06-08
 */
public class PictureJFrame extends JFrame {
    private static final int CTRL_PANEL_HEIGHT = 50;

    private final PictureViewer pictureViewer;
    private JButton fitSizeBtn;

    public PictureJFrame() {
        final int width = 800;
        final int height = 600;
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setMinimumSize(new Dimension(400, 400));
        setLayout(new BorderLayout(0, 0));

        pictureViewer = new PictureViewer();
        pictureViewer.setBounds(0, 0, width, height - CTRL_PANEL_HEIGHT);
        pictureViewer.setPictureChangeListener(() -> {
            fitSizeBtn.setText(pictureViewer.isOriginSize() ? FIT : ORIGIN);
            fitSizeBtn.setEnabled(!pictureViewer.isOriginSize() || !pictureViewer.isFitSize());
        });
        pictureViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 双击切换窗口最大化
                if (e.getClickCount() == 2) {
                    setExtendedState(getExtendedState() == JFrame.MAXIMIZED_BOTH ?
                            JFrame.NORMAL : JFrame.MAXIMIZED_BOTH);
                }
            }
        });
        add(pictureViewer, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBounds((width - 200) / 2, height - CTRL_PANEL_HEIGHT, 200, CTRL_PANEL_HEIGHT);
        add(controlPanel, BorderLayout.SOUTH);

        fitSizeBtn = new JButton();
        fitSizeBtn.addActionListener(e -> {
            if (FIT.equals(fitSizeBtn.getText())) {
                pictureViewer.setPictureFitSize();
            } else {
                pictureViewer.setPictureOriginSize();
            }
        });
        controlPanel.add(fitSizeBtn);

        JButton saveBtn = new JButton(SAVE);
        saveBtn.addActionListener(e -> showPictureSaver());
        controlPanel.add(saveBtn);

        JButton copyBtn = new JButton(COPY);
        copyBtn.addActionListener(e -> Util.copyImage(pictureViewer.getPicture()));
        controlPanel.add(copyBtn);
    }

    private void showPictureSaver() {
        FileDialog fileDialog = new FileDialog(this, SAVE_SCREENSHOT, FileDialog.SAVE);
        fileDialog.setIconImage(getIconImage());
        fileDialog.setFile("Screenshot_" + Util.getCurrentTime().replace(" ", "_") + ".png");
        fileDialog.setVisible(true);
        String dir = fileDialog.getDirectory();
        String fileName = fileDialog.getFile();
        // 校验文件
        if (dir == null || dir.isEmpty() || fileName == null || fileName.isEmpty()) return;

        try {// 保存文件
            File file = new File(dir + fileName);
            ImageIO.write(pictureViewer.getPicture(), "PNG", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPicture(BufferedImage image) {
        // 设置大小及位置
        pictureViewer.setPicture(image);
        int w = image.getWidth();
        int h = image.getHeight();
        int sizeExpected = (int) (Math.min(DeviceUtil.getScreenWidth(), DeviceUtil.getScreenHeight()) * 0.8f);
        if (w < sizeExpected && h < sizeExpected) {
            getContentPane().setPreferredSize(new Dimension(w, h + CTRL_PANEL_HEIGHT));
        } else {
            getContentPane().setPreferredSize(new Dimension(sizeExpected, sizeExpected));
        }
        pack();
        Util.setCenterLocation(this);

        // 显示
        setExtendedState(JFrame.NORMAL);
        setVisible(true);
        toFront();
    }
}
