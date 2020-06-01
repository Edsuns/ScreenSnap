package com.s0n1.screensnap.tools;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Edsuns@qq.com on 2020-05-30
 */
public final class Settings {
    // Default settings
    private static String hotkey = "shift ctrl X";
    private static boolean runInBg = true;

    private static final String FILE_PATH_CONF = "config.properties";
    private static final String KEY_HOTKEY = "Hotkey";
    private static final String KEY_RUN_IN_BG = "RunInBackground";

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
            runInBg = "1".equals(properties.getProperty(KEY_RUN_IN_BG));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
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
            properties.setProperty(KEY_RUN_IN_BG, runInBg ? "1" : "0");
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
        GlobalHotKey.getInstance().setupHotKey();
        save();
    }

    public static boolean isRunInBg() {
        return runInBg;
    }

    public static String getHotkey() {
        return hotkey;
    }
}
