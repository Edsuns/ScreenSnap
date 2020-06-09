package com.s0n1.screensnap.widget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Edsuns@qq.com on 2020-06-08
 */
public class PictureViewer extends JComponent {
    private BufferedImage picture;

    private int xFixed;
    private int yFixed;
    private int widthFixed;
    private int heightFixed;

    private static final float ZOOM_SCALE_MAX = 10;
    private float zoomScaleMin;
    private float zoomScale;

    private int lastMouseX;
    private int lastMouseY;

    public PictureViewer() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setPictureSizeAuto();
            }
        });
        addMouseWheelListener(e -> {
            final float zoomScaleOld = zoomScale;
            if (zoomScale < 1) {
                zoomScale -= (float) e.getPreciseWheelRotation() * 0.05f;
            } else {
                zoomScale -= (float) e.getPreciseWheelRotation() * 0.3f;
            }
            if (zoomScale > ZOOM_SCALE_MAX) {
                zoomScale = ZOOM_SCALE_MAX;
            } else if (zoomScale < zoomScaleMin) {
                zoomScale = zoomScaleMin;
            }
            zoomPicture(zoomScaleOld);
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMouseX = e.getX();
                lastMouseY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isPicSmaller()) {
                    Window window = SwingUtilities.windowForComponent(PictureViewer.this);
                    Point location = window.getLocation();
                    int xNew = location.x + e.getX() - lastMouseX;
                    int yNew = location.y + e.getY() - lastMouseY;
                    window.setLocation(xNew, yNew);
                } else {
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    xFixed += (e.getX() - lastMouseX);
                    yFixed += (e.getY() - lastMouseY);
                    lastMouseX = e.getX();
                    lastMouseY = e.getY();
                    repaint();
                }
            }
        });
    }

    public void setPicture(BufferedImage image) {
        picture = image;
        setPictureSizeAuto();
    }

    public BufferedImage getPicture() {
        return picture;
    }

    /**
     * @return 是否图片尺寸小于容器尺寸
     */
    private boolean isPicSmaller() {
        return widthFixed <= getWidth() && heightFixed <= getHeight();
    }

    /**
     * 根据图片尺寸选择合适的方法缩放图片
     */
    public void setPictureSizeAuto() {
        if (picture == null) return;

        if (picture.getWidth() < getHeight() && picture.getHeight() <= getHeight()) {
            setPictureOriginSize();
        } else {
            setPictureFitSize();
        }
        // 最小缩放比例为Auto比例的一半
        zoomScaleMin = zoomScale * 0.5f;
    }

    /**
     * 根据zoomScale缩放图片大小
     *
     * @param zoomScaleOld 上一次缩放的比例
     */
    private void zoomPicture(float zoomScaleOld) {
        if (picture == null) return;

        int width = picture.getWidth();
        int height = picture.getHeight();
        widthFixed = (int) (width * zoomScale);
        heightFixed = (int) (height * zoomScale);
        Point mouse = getMousePosition();
        int xOnPic = mouse.x - xFixed;
        int yOnPic = mouse.y - yFixed;
        int xOnPicNew = (int) (xOnPic * zoomScale / zoomScaleOld);
        int yOnPicNew = (int) (yOnPic * zoomScale / zoomScaleOld);
        xFixed -= (xOnPicNew - xOnPic);
        yFixed -= (yOnPicNew - yOnPic);
        if (isPicSmaller()) {
            xFixed = (getWidth() - widthFixed) / 2;
            yFixed = (getHeight() - heightFixed) / 2;
        }
        repaint();
    }

    public boolean isOriginSize() {
        if (picture == null) return true;

        return picture.getWidth() == widthFixed && picture.getHeight() == heightFixed;
    }

    public boolean isFitSize() {
        if (picture == null) return true;

        int w = getWidth();
        int h = getHeight();
        return (widthFixed == w && heightFixed <= h) || (heightFixed == h && widthFixed <= w);
    }

    /**
     * 设置图片尺寸为原尺寸
     */
    public void setPictureOriginSize() {
        if (picture == null) return;

        widthFixed = picture.getWidth();
        heightFixed = picture.getHeight();
        xFixed = (getWidth() - widthFixed) / 2;
        yFixed = (getHeight() - heightFixed) / 2;
        zoomScale = 1;
        repaint();
    }

    /**
     * 缩放图片到刚好放进容器
     */
    public void setPictureFitSize() {
        if (picture == null) return;

        int width = picture.getWidth();
        int height = picture.getHeight();
        int sizeW = getWidth();
        int sizeH = getHeight();
        float rateW = (float) width / height;
        float rateSize = (float) sizeW / sizeH;
        boolean shouldZoomByW = rateW > rateSize;
        if (shouldZoomByW) {
            zoomScale = (float) sizeW / width;
            heightFixed = (int) (height * zoomScale);
            widthFixed = sizeW;
            xFixed = 0;
            yFixed = (sizeH - heightFixed) / 2;
        } else {
            zoomScale = (float) sizeH / height;
            widthFixed = (int) (width * zoomScale);
            heightFixed = sizeH;
            xFixed = (sizeW - widthFixed) / 2;
            yFixed = 0;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (picture != null) {
            g.drawImage(picture, xFixed, yFixed, widthFixed, heightFixed, null);
            if (mPictureChangeListener != null) {
                mPictureChangeListener.onPictureChange();
            }
        }
    }

    private PictureChangeListener mPictureChangeListener;

    public void setPictureChangeListener(PictureChangeListener l) {
        this.mPictureChangeListener = l;
    }

    public interface PictureChangeListener {
        void onPictureChange();
    }
}
