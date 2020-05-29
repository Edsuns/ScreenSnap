package com.s0n1.screensnap.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.s0n1.screensnap.ui.UiRes.*;

/**
 * Created by Edsuns@qq.com on 2020-05-29
 */
public class HomeJFrame extends JFrame {

    public HomeJFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setType(Type.UTILITY);
        initTrayIcon();
        setLayout(null);

        JButton closeBtn = new JButton("Exit");
        closeBtn.setBounds((WINDOW_WIDTH - 200) / 2, (WINDOW_HEIGHT - 100) / 2, 200, 100);
        closeBtn.addActionListener(e -> {
            if (mTrayItemClickedListener != null) {
                mTrayItemClickedListener.onItemClicked();
            }
        });
        add(closeBtn);
    }

    private void initTrayIcon() {
        if (!SystemTray.isSupported()) return;

        // 创建弹出菜单
        PopupMenu popup = new PopupMenu();
        //退出程序选项
        MenuItem exitItem = new MenuItem("退出程序");
        exitItem.addActionListener(e -> {
            if (mTrayItemClickedListener != null) {
                mTrayItemClickedListener.onItemClicked();
            }
        });
        popup.add(exitItem);

        TrayIcon trayIcon = new TrayIcon(APP_ICON, "ScreenSnap", popup);// 创建trayIcon
        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 鼠标左键
                if (e.getButton() == MouseEvent.BUTTON1) {
                    setExtendedState(JFrame.NORMAL);
                    setVisible(true);
                    toFront();
                }
            }
        });

        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
        } catch (AWTException e1) {
            e1.printStackTrace();
        }
    }

    private TrayItemClickedListener mTrayItemClickedListener;

    public void setTrayItemListener(TrayItemClickedListener l) {
        mTrayItemClickedListener = l;
    }

    public interface TrayItemClickedListener {
        void onItemClicked();
    }
}
