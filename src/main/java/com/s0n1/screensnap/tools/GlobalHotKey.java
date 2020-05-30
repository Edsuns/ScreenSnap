package com.s0n1.screensnap.tools;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

import javax.swing.*;

/**
 * Created by Edsuns@qq.com on 2020-05-26
 */
public class GlobalHotKey implements HotKeyListener {
    private Provider hotKeyProvider;

    private static GlobalHotKey instance;

    public static void newInstance() {
        if (instance != null) {
            instance.stopHotKey();
        }
        instance = new GlobalHotKey();
        instance.setupHotKey();
    }

    public static GlobalHotKey getInstance() {
        return instance;
    }

    private GlobalHotKey() {
    }

    public void setupHotKey() {
        final GlobalHotKey instance = this;
        new Thread(() -> {
            try {
                if (hotKeyProvider == null) {
                    hotKeyProvider = Provider.getCurrentProvider(false);
                }
                hotKeyProvider.reset();
                hotKeyProvider.register(KeyStroke.getKeyStroke(Settings.getHotkey()), instance);
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
        if (mHotKeyListener != null) {
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
