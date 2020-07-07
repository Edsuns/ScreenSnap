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

    private GlobalHotKey() {
    }

    public static GlobalHotKey getInstance() {
        if (instance == null) {
            instance = new GlobalHotKey();
        }
        return instance;
    }

    /**
     * 加载设置（重置+注册）
     *
     * @param hotkey 热键
     */
    public void load(String hotkey) {
        final GlobalHotKey instance = this;
        new Thread(() -> {
            try {
                if (hotKeyProvider == null) {
                    hotKeyProvider = Provider.getCurrentProvider(false);
                }
                hotKeyProvider.reset();
                hotKeyProvider.register(KeyStroke.getKeyStroke(hotkey), instance);
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
