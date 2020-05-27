package com.s0n1.capturer.tools;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Created by Edsuns@qq.com on 2020-05-26
 */
public class GlobalHotKey implements HotKeyListener {
    private Provider hotKeyProvider;
    private String shotHotKey = "control alt X";

    public GlobalHotKey() {
        initHotKey();
    }

    public void setShotHotKey(String shotHotKey) {
        this.shotHotKey = shotHotKey;
        initHotKey();
    }

    private void initHotKey() {
        final GlobalHotKey instance = this;
        new Thread(() -> {
            try {
                if (hotKeyProvider == null) {
                    hotKeyProvider = Provider.getCurrentProvider(false);
                }
                hotKeyProvider.reset();
                hotKeyProvider.register(KeyStroke.getKeyStroke(shotHotKey), instance);
            } catch (Exception e) {
                hotKeyProvider = null;
                e.printStackTrace();
            }
        }).start();
    }

    public void stopHotKey() {
        new Thread(() -> {
            try {
                if (hotKeyProvider != null) {
                    hotKeyProvider.reset();
                    hotKeyProvider.stop();
                }
            } catch (Exception e) {
                hotKeyProvider = null;
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onHotKey(HotKey hotKey) {
        if (hotKey.keyStroke.getKeyCode() == KeyEvent.VK_X && mHotKeyListener != null) {
            mHotKeyListener.onPickColorHotKey();
        }
    }

    private HotKeyListener mHotKeyListener;

    public void setHotKeyListener(HotKeyListener l) {
        mHotKeyListener = l;
    }

    public interface HotKeyListener {
        void onPickColorHotKey();
    }
}
