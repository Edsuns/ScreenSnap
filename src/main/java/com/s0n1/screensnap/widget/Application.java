package com.s0n1.screensnap.widget;

import com.s0n1.screensnap.tools.Settings;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ResourceBundle;

/**
 * Created by Edsuns@qq.com on 2020-05-31
 */
public abstract class Application {
    private static Application instance;

    public Application() {
        if (instance == null) {
            instance = this;
        }
    }

    public static Application instance() {
        return instance;
    }

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("language", Settings.getLocale());

    public static ResourceBundle res() {
        return resourceBundle;
    }

    public static void setResourceBundle(ResourceBundle resourceBundle) {
        Application.resourceBundle = resourceBundle;
    }

    public void onAppClose(boolean runInBg) {
    }

    /**
     * 重启程序
     */
    public void restart() {
        StringBuilder cmd = new StringBuilder();
        cmd.append(System.getProperty("java.home"))
                .append(File.separator).append("bin").append(File.separator).append("java ");
        for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            cmd.append(jvmArg).append(" ");
        }
        cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
        cmd.append(getClass().getName()).append(" ");
//        for (String arg : args) {
//            cmd.append(arg).append(" ");
//        }
        try {
            Runtime.getRuntime().exec(cmd.toString());
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
