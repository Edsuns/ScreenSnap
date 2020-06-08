package com.s0n1.screensnap.ui;

import com.s0n1.screensnap.util.AppUtil;
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
    private final PictureViewer pictureViewer;
    private JButton fitSizeBtn;

    public PictureJFrame() {
        final int width = 800;
        final int height = 600;
        final int ctrlHeight = 50;
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(300, 200));
        setLayout(new BorderLayout(0, 0));

        pictureViewer = new PictureViewer();
        pictureViewer.setBounds(0, 0, width, height - ctrlHeight);
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
        pictureViewer.addMouseWheelListener(e -> fitSizeBtn.setText(pictureViewer.isOriginSize() ? FIT : ORIGIN));
        add(pictureViewer, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBounds((width - 200) / 2, height - ctrlHeight, 200, ctrlHeight);
        add(controlPanel, BorderLayout.SOUTH);

        fitSizeBtn = new JButton(FIT);
        fitSizeBtn.addActionListener(e -> {
            if (pictureViewer.isOriginSize()) {
                pictureViewer.setPictureFitSize();
                fitSizeBtn.setText(ORIGIN);
            } else {
                pictureViewer.setPictureOriginSize();
                fitSizeBtn.setText(FIT);
            }
        });
        controlPanel.add(fitSizeBtn);

        JButton saveBtn = new JButton(SAVE);
        saveBtn.addActionListener(e -> showPictureSaver());
        controlPanel.add(saveBtn);

        JButton copyBtn = new JButton(COPY);
        copyBtn.addActionListener(e -> AppUtil.copyImage(pictureViewer.getPicture()));
        controlPanel.add(copyBtn);

        pack();
    }

    private void showPictureSaver() {
        FileDialog fileDialog = new FileDialog(this, SAVE_SCREENSHOT, FileDialog.SAVE);
        fileDialog.setIconImage(getIconImage());
        fileDialog.setVisible(true);
        String dir = fileDialog.getDirectory();
        String fileName = fileDialog.getFile();
        // 校验文件
        if (dir == null || dir.isEmpty() || fileName == null || fileName.isEmpty()) return;

        if (!fileName.endsWith(".png")) fileName += ".png";
        try {
            File file = new File(dir + fileName);
            ImageIO.write(pictureViewer.getPicture(), "PNG", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPicture(BufferedImage image) {
        pictureViewer.setPicture(image);
        setExtendedState(JFrame.NORMAL);
        setVisible(true);
        toFront();
    }
}
