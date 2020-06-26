package com.s0n1.screensnap.ui;

import com.s0n1.screensnap.util.FrameUtil;
import com.s0n1.screensnap.util.Util;
import com.s0n1.screensnap.widget.Application;
import com.s0n1.screensnap.widget.PictureViewer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.s0n1.screensnap.ui.UiRes.ORIGIN;

/**
 * Created by Edsuns@qq.com on 2020-06-08
 */
public class PictureJFrame extends JFrame {
    private final PictureViewer pictureViewer;
    private JButton fitSizeBtn;

    public PictureJFrame() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setMinimumSize(new Dimension(400, 400));
        setLayout(new BorderLayout(0, 0));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                pictureViewer.setPicture(null, null);
            }
        });

        pictureViewer = new PictureViewer();
        pictureViewer.setPictureChangeListener(() -> {
            // 默认FIT，即isFitSize和isOriginSize都否时设置为FIT
            fitSizeBtn.setText(pictureViewer.isFitSize() ? ORIGIN : Application.res().getString("fit"));
            fitSizeBtn.setEnabled(!pictureViewer.isOriginSize() || !pictureViewer.isFitSize());
        });
        pictureViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 双击切换窗口最大化
                if (e.getClickCount() == 2) {
                    if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                        // 通过showPicture设置最小化能同时恢复合适的尺寸
                        showPicture(pictureViewer.getPicture());
                    } else {
                        // 最大化
                        setExtendedState(JFrame.MAXIMIZED_BOTH);
                    }
                }
            }
        });
        add(pictureViewer, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        add(controlPanel, BorderLayout.SOUTH);

        fitSizeBtn = new JButton();
        fitSizeBtn.addActionListener(e -> {
            if (Application.res().getString("fit").equals(fitSizeBtn.getText())) {
                pictureViewer.setPictureFitSize();
            } else {
                pictureViewer.setPictureOriginSize();
            }
        });
        controlPanel.add(fitSizeBtn);

        JButton rotateBtn = new JButton(Application.res().getString("rotate"));
        rotateBtn.addActionListener(e -> pictureViewer.rotatePicture());
        controlPanel.add(rotateBtn);

        JButton saveBtn = new JButton(Application.res().getString("save"));
        saveBtn.addActionListener(e -> savePicture());
        controlPanel.add(saveBtn);

        JButton copyBtn = new JButton(Application.res().getString("copy"));
        copyBtn.addActionListener(e -> Util.copyImage(pictureViewer.getPicture()));
        controlPanel.add(copyBtn);
    }

    private void savePicture() {
        FileDialog fileDialog = new FileDialog(this, Application.res().getString("save_screenshot"), FileDialog.SAVE);
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
        int widthMax = (int) (FrameUtil.getScreenWidth() * 0.8f);
        int heightMax = (int) (FrameUtil.getScreenHeight() * 0.8f);
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        if (imageWidth > widthMax || imageHeight > heightMax) {// 如果超过限制的尺寸
            Dimension fitSize = PictureViewer.calcFitSize(image, widthMax, heightMax);
            pictureViewer.setPreferredSize(fitSize);
            pictureViewer.setPicture(image, fitSize);
        } else {
            Dimension originSize = new Dimension(imageWidth, imageHeight);
            pictureViewer.setPreferredSize(originSize);
            pictureViewer.setPicture(image, originSize);
        }
        pack();// 必须

        FrameUtil.setCenterLocation(this);

        // 显示
        setExtendedState(JFrame.NORMAL);
        setVisible(true);
        toFront();
    }
}
