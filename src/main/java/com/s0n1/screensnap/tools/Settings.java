package com.s0n1.screensnap.tools;

import javax.swing.*;
import java.io.*;

public final class Settings {
    private static boolean runInBg = true;
    private static String hotkey = "control alt X";

    public static void loadSettings() {
        try {
            File file = new File("./Settings.txt");
            if (!file.exists()) return;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String[] lines = new String[2];
            Object[] object = reader.lines().toArray();
            if (object.length >= 2) {
                lines[0] = String.valueOf(object[0]);
                lines[1] = String.valueOf(object[1]);
            }

            String hotkeyFromLocal = lines[0];
            KeyStroke keyStroke = KeyStroke.getKeyStroke(hotkeyFromLocal);
            if (keyStroke != null) {
                hotkey = hotkeyFromLocal;
            }
            runInBg = "1".equals(lines[1]);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveSettings() {
        File file = new File("./Settings.txt");
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    System.out.println("Failed to create new file!");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(hotkey);
            bufferedWriter.newLine();
            bufferedWriter.write(runInBg ? "1" : "0");
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setRunInBg(boolean runInBg) {
        Settings.runInBg = runInBg;
        saveSettings();
    }

    public static void setHotkey(String hotkey) {
        Settings.hotkey = hotkey;
        GlobalHotKey.getInstance().setupHotKey();
        saveSettings();
    }

    public static boolean isRunInBg() {
        return runInBg;
    }

    public static String getHotkey() {
        return hotkey;
    }
}
