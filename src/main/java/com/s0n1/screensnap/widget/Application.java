package com.s0n1.screensnap.widget;

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

    public static Application getInstance() {
        return instance;
    }

    public void onAppClose(boolean runInBg) {
    }
}
