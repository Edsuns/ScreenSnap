package com.s0n1.screensnap.tools;

import com.s0n1.screensnap.widget.Application;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by Edsuns@qq.com on 2020-05-30
 */
public final class Settings {
    // Default settings
    private static String hotkey = "shift ctrl X";
    private static boolean runInBg = true;
    private static Locale locale = Locale.getDefault();

    private static final String FILE_PATH_CONF = "config.properties";
    private static final String KEY_HOTKEY = "Hotkey";
    private static final String KEY_RUN_IN_BG = "RunInBackground";
    private static final String KEY_LANGUAGE = "Language";

    public static void load() {
        try {
            FileInputStream inputStream = new FileInputStream(FILE_PATH_CONF);
            Properties properties = new Properties();
            properties.load(inputStream);

            // Hotkey
            String hotkeyRead = properties.getProperty(KEY_HOTKEY);
            if (KeyStroke.getKeyStroke(hotkeyRead) != null) {
                hotkey = hotkeyRead;
            }
            // Run in background
            runInBg = "true".equals(properties.getProperty(KEY_RUN_IN_BG));
            // Language
            String language = properties.getProperty(KEY_LANGUAGE);
            if (language != null) {
                locale = Locale.forLanguageTag(language);
                ResourceBundle resource = ResourceBundle.getBundle("language", locale);
                // TODO 检查方法不严谨，不能防止注入
                if (resource.getLocale().equals(locale)) {// 如果获取得到locale
                    Application.setResourceBundle(resource);// 设置ResourceBundle
                }// 不setResourceBundle即取初始值
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        // 存在FileNotFoundException，必须外置
        GlobalHotKey.getInstance().load(hotkey);
    }

    public static void save() {
        try {
            File file = new File(FILE_PATH_CONF);
            if (!file.exists() && !file.createNewFile()) {
                throw new IOException("Fail to create new file: " + FILE_PATH_CONF);
            }
            FileInputStream inputStream = new FileInputStream(file);
            FileOutputStream outputStream = new FileOutputStream(file);
            Properties properties = new Properties();
            properties.load(inputStream);
            // Hotkey
            properties.setProperty(KEY_HOTKEY, hotkey);
            // Run in background
            properties.setProperty(KEY_RUN_IN_BG, runInBg ? "true" : "false");
            // Locale
            properties.setProperty(KEY_LANGUAGE, locale.toLanguageTag());
            // Save to file
            properties.store(outputStream, null);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void setRunInBg(boolean runInBg) {
        Settings.runInBg = runInBg;
        save();
    }

    public static void setHotkey(String hotkey) {
        Settings.hotkey = hotkey;
        GlobalHotKey.getInstance().load(hotkey);
        save();
    }

    public static void setLanguage(Locale locale) {
        Settings.locale = locale;
        save();
        Application.instance().restart();
    }

    public static boolean isRunInBg() {
        return runInBg;
    }

    public static String getHotkey() {
        return hotkey;
    }

    public static Locale getLocale() {
        return locale;
    }
}
