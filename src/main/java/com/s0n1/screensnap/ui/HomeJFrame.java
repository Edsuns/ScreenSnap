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
import java.util.Locale;

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
        settingsPanel.setPreferredSize(new Dimension(350, 190));
        settingsPanel.setLayout(null);
        borderPane.add(settingsPanel);

        // 热键设置
        JLabel hotKeyLabel = new JLabel(Application.res().getString("hotkey"));
        hotKeyLabel.setBounds(0, 0, 72, 30);
        settingsPanel.add(hotKeyLabel);

        JTextField hotKeyText = new JTextField();
        hotKeyText.setBounds(70, 0, 140, 30);
        hotKeyText.setHorizontalAlignment(SwingConstants.CENTER);
        hotKeyText.setText(Settings.getHotkey());
        hotKeyText.setEditable(false);
        settingsPanel.add(hotKeyText);
        dialog.setHotkeyChangeListener(hotKeyText::setText);

        JButton editHotKeyBtn = new JButton(Application.res().getString("change"));
        editHotKeyBtn.setBounds(230, 0, 100, 30);
        editHotKeyBtn.addActionListener(e -> {
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });
        settingsPanel.add(editHotKeyBtn);

        // 语言设置
        JLabel languageLabel = new JLabel(Application.res().getString("language_setting"));
        languageLabel.setBounds(0, 60, 200, 30);
        settingsPanel.add(languageLabel);

        String[] languageName = {"English", "简体中文"};
        String[] languageTag = {"en", "zh-CN"};
        JComboBox<String> comboBox = new JComboBox<>(languageName);
        comboBox.setBounds(230, 60, 100, 30);
        // 获取设置的语言
        String setTag = Settings.getLocale().toLanguageTag();
        int i = 0;
        for (; i < languageName.length; i++) {
            if (languageTag[i].contains(setTag)) break;// 使用contains能匹配相关的语言
        }
        final int setIndex = Math.min(i, comboBox.getItemCount() - 1);
        comboBox.setSelectedIndex(setIndex);
        // 必须在后，因为setSelectedIndex会触发Action导致程序重启，最终出错
        comboBox.addActionListener(e -> {
            String s = (String) comboBox.getSelectedItem();
            int index = 0;// 获取选中的语言
            for (; index < languageName.length; index++) {
                if (languageName[index].equals(s)) break;
            }
            if (!languageTag[setIndex].equals(languageTag[index])) {// 如果语言选择有改变
                Settings.setLanguage(Locale.forLanguageTag(languageTag[index]));
            }
        });
        settingsPanel.add(comboBox);

        // 后台运行设置
        JCheckBox backgroundCheck = new JCheckBox(Application.res().getString("run_in_bg"));
        backgroundCheck.setBounds(0, 120, 220, 30);
        backgroundCheck.setSelected(Settings.isRunInBg());
        backgroundCheck.addItemListener(e -> {
            boolean runInBg = e.getStateChange() == ItemEvent.SELECTED;
            if (!runInBg) disableRunInBg();
            Settings.setRunInBg(runInBg);
            System.out.println("RunInBg: " + runInBg);
        });
        settingsPanel.add(backgroundCheck);

        JButton exitBtn = new JButton(Application.res().getString("exit"));
        exitBtn.setBounds(230, 120, 100, 30);
        exitBtn.addActionListener(e -> Application.instance().onAppClose(false));
        settingsPanel.add(exitBtn);

        // 底部
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottom.setBounds(260, 170, 100, 20);
        settingsPanel.add(bottom);

        // 版本号
        JLabel version = new JLabel(VERSION);
        bottom.add(version);

        // About链接
        JLabel aboutLabel = new UrlLabel(Application.res().getString("about"), ABOUT_URL);
        aboutLabel.setToolTipText(ABOUT_URL);
        bottom.add(aboutLabel);

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
        MenuItem exitItem = new MenuItem(Application.res().getString("exit"));
        exitItem.addActionListener(e -> Application.instance().onAppClose(false));
        popup.add(exitItem);

        trayIcon = new TrayIcon(APP_ICON, Application.res().getString("app_name"), popup);// 创建trayIcon
        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 鼠标左键
                if (e.getButton() == MouseEvent.BUTTON1) {
                    setExtendedState(JFrame.NORMAL);
                    setVisible(!isActive());
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
