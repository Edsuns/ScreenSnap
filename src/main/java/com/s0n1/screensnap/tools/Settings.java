package com.s0n1.screensnap.tools;

import com.s0n1.screensnap.widget.Application;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by Edsuns@qq.com on 2020-05-30
 */
public final class Settings {
    // Default settings
    private static String hotkey = "shift ctrl X";
    private static boolean runInBg = true;
    private static Locale locale = Application.res().getLocale();

    private static final String CONF_FILE_PATH = "config.properties";
    private static final String KEY_HOTKEY = "Hotkey";
    private static final String KEY_RUN_IN_BG = "RunInBackground";
    private static final String KEY_LANGUAGE = "Language";

    public static void load() {
        try {
            FileInputStream inputStream = new FileInputStream(CONF_FILE_PATH);
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
                try {
                    Locale l = Locale.forLanguageTag(language);
                    // 如果抛MissingResourceException就不会设置ResourceBundle和locale
                    ResourceBundle resource = ResourceBundle.getBundle("language", l);
                    // 设置ResourceBundle和locale
                    Application.setResourceBundle(resource);
                    locale = l;
                } catch (MissingResourceException ignored) {
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        // 存在FileNotFoundException，必须外置
        GlobalHotKey.getInstance().load(hotkey);
    }

    public static void save() {
        try {
            File file = new File(CONF_FILE_PATH);
            if (!file.exists() && !file.createNewFile()) {
                throw new IOException("Fail to create new file: " + CONF_FILE_PATH);
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
