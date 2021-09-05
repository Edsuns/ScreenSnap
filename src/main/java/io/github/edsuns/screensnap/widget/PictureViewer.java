package io.github.edsuns.screensnap.widget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * 图片查看JComponent
 * Created by Edsuns@qq.com on 2020-06-08
 */
public class PictureViewer extends JComponent {
    private BufferedImage picture;
    // 缓存缩放的图片提高性能
    private Image zoomPicture;

    private int fixedX;
    private int fixedY;
    private int fixedWidth;
    private int fixedHeight;

    private static final int ZOOM_SCALE_MAX = 10;
    private static final float ZOOM_SCALE_MIN = 0.1f;
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
            float scale = zoomScale;
            if (scale < 1) {
                scale -= (float) e.getPreciseWheelRotation() * 0.05f;
            } else {
                scale -= (float) e.getPreciseWheelRotation() * 0.3f;
            }
            if (scale > ZOOM_SCALE_MAX) {
                scale = ZOOM_SCALE_MAX;
            } else if (scale < ZOOM_SCALE_MIN) {
                scale = ZOOM_SCALE_MIN;
            }
            zoomPicture(scale);
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMouseX = e.getX();
                lastMouseY = e.getY();
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isPicSmaller()) {
                    // 移动窗口
                    Window window = SwingUtilities.windowForComponent(PictureViewer.this);
                    Point location = window.getLocation();
                    int newX = location.x + e.getX() - lastMouseX;
                    int newY = location.y + e.getY() - lastMouseY;
                    window.setLocation(newX, newY);
                } else {
                    // 移动图片
                    fixedX += (e.getX() - lastMouseX);
                    fixedY += (e.getY() - lastMouseY);
                    lastMouseX = e.getX();
                    lastMouseY = e.getY();
                    repaint();
                }
            }
        });
    }

    public void setPicture(BufferedImage image, Dimension fixedSize) {
        picture = image;
        if (picture == null) {
            repaint();
            return;
        }

        fixedWidth = fixedSize.width;
        fixedHeight = fixedSize.height;
        zoomScale = (float) fixedHeight / image.getWidth();
        zoomPicture = picture.getScaledInstance(fixedWidth, fixedHeight, Image.SCALE_SMOOTH);
        fixedX = (getWidth() - fixedWidth) / 2;
        fixedY = (getHeight() - fixedHeight) / 2;
        repaint();
    }

    public BufferedImage getPicture() {
        return picture;
    }

    /**
     * 根据图片尺寸选择合适的方法缩放图片
     */
    private void setPictureSizeAuto() {
        if (picture == null) return;

        if (picture.getWidth() < getActualHeight() && picture.getHeight() <= getActualHeight()) {
            setPictureOriginSize();
        } else {
            setPictureFitSize();
        }
    }

    /**
     * 设置图片尺寸为原尺寸
     */
    public void setPictureOriginSize() {
        if (picture == null) return;

        fixedWidth = picture.getWidth();
        fixedHeight = picture.getHeight();
        fixedX = (getWidth() - fixedWidth) / 2;
        fixedY = (getHeight() - fixedHeight) / 2;
        zoomScale = 1;
        repaint();
    }

    /**
     * 缩放图片到刚好放进此部件
     */
    public void setPictureFitSize() {
        if (picture == null) return;

        int width = getWidth();
        int height = getActualHeight();
        Dimension fitSize = calcFitSize(picture, width, height);
        fixedWidth = fitSize.width;
        fixedHeight = fitSize.height;
        zoomScale = (float) fitSize.width / picture.getWidth();
        zoomPicture = picture.getScaledInstance(fixedWidth, fixedHeight, Image.SCALE_SMOOTH);
        // 居中显示
        fixedX = (width - fixedWidth) / 2;
        fixedY = (height - fixedHeight) / 2;
        repaint();
    }

    /**
     * 计算刚好可以放进Max的尺寸
     *
     * @param image     源
     * @param widthMax  缩放依据
     * @param heightMax 缩放依据
     * @return 结果
     */
    public static Dimension calcFitSize(BufferedImage image, int widthMax, int heightMax) {
        if (image == null) return new Dimension(0, 0);

        int w = image.getWidth();
        int h = image.getHeight();
        float scale = (float) heightMax / h;
        int newWidth = (int) (w * scale);
        if (newWidth <= widthMax) {
            w = (int) (w * scale);
            h = heightMax;
        } else {
            scale = (float) widthMax / w;
            w = widthMax;
            h = (int) (h * scale);
        }
        return new Dimension(w, h);
    }

    /**
     * 顺时针旋转90度
     */
    public void rotatePicture() {
        if (picture == null) return;

        // 生成旋转后的图片，比Graphics2D.rotate（长或宽为奇数会出现黑边）好
        int width = picture.getWidth();
        int height = picture.getHeight();
        BufferedImage newImage = new BufferedImage(height, width, picture.getType());
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                newImage.setRGB(height - 1 - j, i, picture.getRGB(i, j));
        picture = newImage;

        // 设置宽高
        int oldHeight = fixedHeight;
        fixedHeight = fixedWidth;
        fixedWidth = oldHeight;
        // 中心旋转的新坐标
        int xOnPicOld = getWidth() / 2 - fixedX;
        int yOnPicOld = getActualHeight() / 2 - fixedY;
        fixedX -= oldHeight - xOnPicOld - yOnPicOld;
        fixedY -= xOnPicOld - yOnPicOld;
        zoomPicture = picture.getScaledInstance(fixedWidth, fixedHeight, Image.SCALE_SMOOTH);
        repaint();
    }

    /**
     * 以鼠标为中心缩放图片
     *
     * @param scaleNew 缩放比例
     */
    private void zoomPicture(float scaleNew) {
        if (picture == null) return;

        int width = picture.getWidth();
        int height = picture.getHeight();
        fixedWidth = (int) (width * scaleNew);
        fixedHeight = (int) (height * scaleNew);
        Point mouse = getMousePosition();
        int xOnPic = mouse.x - fixedX;
        int yOnPic = mouse.y - fixedY;
        int xOnPicNew = (int) (xOnPic * scaleNew / zoomScale);
        int yOnPicNew = (int) (yOnPic * scaleNew / zoomScale);
        fixedX -= (xOnPicNew - xOnPic);
        fixedY -= (yOnPicNew - yOnPic);
        if (isPicSmaller()) {
            fixedX = (getWidth() - fixedWidth) / 2;
            fixedY = (getHeight() - fixedHeight) / 2;
        }
        zoomScale = scaleNew;
        zoomPicture = picture.getScaledInstance(fixedWidth, fixedHeight, Image.SCALE_SMOOTH);
        repaint();
    }

    private int getActualHeight() {
        // getHeight获取到的总比实际的高度少1，加回去
        return getHeight() + 1;
    }

    public boolean isOriginSize() {
        if (picture == null) return true;

        return picture.getWidth() == fixedWidth && picture.getHeight() == fixedHeight;
    }

    public boolean isFitSize() {
        if (picture == null) return true;

        int w = getWidth();
        int h = getActualHeight();
        return (fixedWidth == w && fixedHeight <= h) || (fixedHeight == h && fixedWidth <= w);
    }

    /**
     * @return 是否图片尺寸小于容器尺寸
     */
    private boolean isPicSmaller() {
        return fixedWidth <= getWidth() && fixedHeight <= getActualHeight();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (picture != null) {
            if (zoomScale < 1) {// 缩放小于1需要平滑处理，以去除锯齿
                g.drawImage(zoomPicture, fixedX, fixedY, null);
            } else {
                g.drawImage(picture, fixedX, fixedY, fixedWidth, fixedHeight, null);
            }
            setCursor(isPicSmaller() ? Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR) :
                    Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
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
