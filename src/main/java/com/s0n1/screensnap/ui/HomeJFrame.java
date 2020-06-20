package com.s0n1.screensnap.ui;

import com.s0n1.screensnap.tools.Settings;
import com.s0n1.screensnap.widget.Application;
import com.s0n1.screensnap.widget.UrlLabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.s0n1.screensnap.ui.UiRes.*;

/**
 * Created by Edsuns@qq.com on 2020-05-29
 */
public class HomeJFrame extends JFrame {

    private final HotkeyDialog dialog;

    public HomeJFrame(HotkeyDialog hotkeyDialog) {
        dialog = hotkeyDialog;
        setDefaultCloseOperation(Settings.isRunInBg() ? JFrame.DO_NOTHING_ON_CLOSE : JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        initPanel();
    }

    private void initPanel() {
        // 边框Panel
        JPanel borderPane = new JPanel();
        borderPane.setBorder(new EmptyBorder(40, 40, 5, 10));
        borderPane.setLayout(new GridLayout(1, 1));
        setContentPane(borderPane);

        // 设置Panel
        // settingsPanel内容 宽350 高140
        JPanel settingsPanel = new JPanel();
        settingsPanel.setPreferredSize(new Dimension(350, 140));
        settingsPanel.setLayout(null);
        borderPane.add(settingsPanel);

        // 热键设置
        JLabel hotKeyLabel = new JLabel(HOTKEY);
        hotKeyLabel.setBounds(0, 0, 72, 30);
        settingsPanel.add(hotKeyLabel);

        JTextField hotKeyText = new JTextField();
        hotKeyText.setBounds(80, 0, 140, 30);
        hotKeyText.setHorizontalAlignment(SwingConstants.CENTER);
        hotKeyText.setText(Settings.getHotkey());
        hotKeyText.setEditable(false);
        settingsPanel.add(hotKeyText);
        dialog.setHotkeyChangeListener(hotKeyText::setText);

        JButton editHotKeyBtn = new JButton(CHANGE);
        editHotKeyBtn.setBounds(240, 0, 90, 30);
        editHotKeyBtn.addActionListener(e -> {
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });
        settingsPanel.add(editHotKeyBtn);

        // 后台运行设置
        JCheckBox backgroundCheck = new JCheckBox(RUN_IN_BG);
        backgroundCheck.setBounds(0, 60, 220, 30);
        backgroundCheck.setSelected(Settings.isRunInBg());
        backgroundCheck.addItemListener(e -> {
            boolean runInBg = e.getStateChange() == ItemEvent.SELECTED;
            if (!runInBg) disableRunInBg();
            Settings.setRunInBg(runInBg);
            System.out.println("RunInBg: " + runInBg);
        });
        settingsPanel.add(backgroundCheck);

        JButton exitBtn = new JButton(EXIT);
        exitBtn.setBounds(240, 60, 90, 30);
        exitBtn.addActionListener(e -> Application.getInstance().onAppClose(false));
        settingsPanel.add(exitBtn);

        // About链接
        JLabel aboutLabel = new UrlLabel(ABOUT, ABOUT_URL);
        aboutLabel.setToolTipText(ABOUT_URL);
        aboutLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        aboutLabel.setBounds(300, 120, 50, 20);
        settingsPanel.add(aboutLabel);

        pack();
    }

    private TrayIcon trayIcon;

    public void disableRunInBg() {
        if (!SystemTray.isSupported()) return;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SystemTray tray = SystemTray.getSystemTray();
        tray.remove(trayIcon);
        trayIcon = null;
    }

    public void enableRunInBg() {
        if (!SystemTray.isSupported()) return;
        // 防止重复创建
        if (trayIcon != null) return;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // 开始创建任务栏小图标
        // 创建弹出菜单
        PopupMenu popup = new PopupMenu();
        //退出程序选项
        MenuItem exitItem = new MenuItem(EXIT);
        exitItem.addActionListener(e -> Application.getInstance().onAppClose(false));
        popup.add(exitItem);

        trayIcon = new TrayIcon(APP_ICON, APP_NAME, popup);// 创建trayIcon
        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 鼠标左键
                if (e.getButton() == MouseEvent.BUTTON1) {
                    setExtendedState(JFrame.NORMAL);
                    setVisible(!isVisible());
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
}
