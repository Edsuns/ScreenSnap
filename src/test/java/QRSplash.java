import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.lang.reflect.Field;

public class QRSplash extends JWindow implements ActionListener {

    private final int windowX;
    private final int windowY;
    private final int windowW;
    private final int windowH;

    private final double scaleX;
    private final double scaleY;

    private final Timer timer;

    private static final double FPS = 1.0 / 15 * 1000;
    private static final double ANIMATION_TIME = 0.5;
    private static final int ANIMATION_STEPS = (int) (ANIMATION_TIME * FPS);
    private final Stopwatch sw;

    private int flashStep;

    public QRSplash() {

        // setResizable(false);
        // this.setUndecorated(true);
        this.setBackground(new Color(100, 0, 0, 50));
        this.setVisible(true);
        // 不显示任务栏图标
        // setType(Type.UTILITY);

        // 兼容 Windows
        // setExtendedState(JFrame.MAXIMIZED_BOTH);

        windowX = User32.INSTANCE.GetSystemMetrics(WinUser.SM_XVIRTUALSCREEN);
        windowY = User32.INSTANCE.GetSystemMetrics(WinUser.SM_YVIRTUALSCREEN);
        windowW = User32.INSTANCE.GetSystemMetrics(WinUser.SM_CXVIRTUALSCREEN);
        windowH = User32.INSTANCE.GetSystemMetrics(WinUser.SM_CYVIRTUALSCREEN);

        AffineTransform defaultTransform = getGraphicsConfiguration().getDefaultTransform();
        scaleX = defaultTransform.getScaleX();
        scaleY = defaultTransform.getScaleY();
        setLocation(new Point((int)(windowX/scaleX),(int)(windowY/scaleY)));
        setSize(new Dimension((int)(windowW-1), (int)(windowH)));

        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setAlwaysOnTop(true);
        // setExtendedState(JFrame.MAXIMIZED_BOTH);
        // setOpacity(0.1f);

        timer = new Timer((int) (ANIMATION_TIME * 1000 / ANIMATION_STEPS), this);
        sw = Stopwatch.createUnstarted();

        start();
    }
    private WinDef.POINT getCursorPos() {
        User32 user32 = User32.INSTANCE;
        WinDef.POINT point = new WinDef.POINT();
        user32.GetCursorPos(point);
        return point;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (flashStep) {
            case 0:
                timer.setDelay(100);
                // this.setVisible(false);
                break;
            case 1:
                timer.setDelay(50);
                // this.setVisible(true);
                break;
            case 2:
            case 4:
                // this.setVisible(false);
                break;
            case 3:
            case 5:
                // this.setVisible(true);
                break;
            default:
                sw.stop();
                timer.stop();
                this.dispose();
                break;
        }
        flashStep++;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g.create(); // Create a copy of the Graphics context
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.red);
        g2d.setStroke(new BasicStroke(5));

        WinDef.POINT point = getCursorPos();

        System.out.println("x:"+point.x +",y:"+ point.y);


        int x =point.x - windowX ;
        int y = point.y - windowY ;

        int ovalWidth = (int) (50 * 2);
        int ovalHeight = (int) (50 * 2);

        g2d.scale(1/scaleX,1/scaleY);

        g2d.drawRect(x, y, ovalWidth, ovalHeight);

        g2d.drawRect(0, 0, (1680), (int)(1050));

        g2d.drawRect((int)(1680), -(int)(windowY ), (int)(1920), (int)(1080));

        g2d.setColor(new Color(128, 0, 128));  // Purple color
        g2d.setFont(new Font("Arial", Font.PLAIN, (int) (14 * 1.5)));

        int textX = x + 2;
        int textY = y - 10;

        // g2d.drawString("TTSSSSSSSSS", textX, textY);

        g2d.dispose(); // Dispose of the Graphics context
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public void start() {
        sw.start();
        timer.start();
    }
}
