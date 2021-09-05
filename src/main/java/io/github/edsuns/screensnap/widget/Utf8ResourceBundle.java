package io.github.edsuns.screensnap.widget;

import io.github.edsuns.screensnap.util.FrameUtil;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * 用于解决JRE1.8及更低版本获取ResourceBundle编码不是UTF-8的问题
 * 使用: Utf8ResourceBundle.getBundle(baseName, locale)
 * <p>
 * Created by Edsuns@qq.com on 6/29/2020.
 */
public abstract class Utf8ResourceBundle {
    public static ResourceBundle getBundle(final String baseName) {
        return createUtf8ResourceBundle(
                ResourceBundle.getBundle(baseName));
    }

    public static ResourceBundle getBundle(final String baseName,
                                           final Locale locale) {
        return createUtf8ResourceBundle(
                ResourceBundle.getBundle(baseName, locale));
    }

    private static ResourceBundle createUtf8ResourceBundle(
            final ResourceBundle bundle) {
        if (!FrameUtil.isOldVersionJava) {
            return bundle;// Java9及更高版本不需要处理
        }
        return new Utf8PropertyResourceBundle(bundle);
    }

    private static class Utf8PropertyResourceBundle extends ResourceBundle {
        private final ResourceBundle bundle;

        private Utf8PropertyResourceBundle(final ResourceBundle bundle) {
            this.bundle = bundle;
        }

        @Override
        public Enumeration<String> getKeys() {
            return bundle.getKeys();
        }

        @Override
        protected Object handleGetObject(final String key) {
            final String value = bundle.getString(key);
            // 转换成UTF-8
            return new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        }

        @Override
        public Locale getLocale() {
            return bundle.getLocale();
        }

        @Override
        public String getBaseBundleName() {
            return bundle.getBaseBundleName();
        }

        @Override
        public boolean containsKey(String key) {
            return bundle.containsKey(key);
        }

        @Override
        public Set<String> keySet() {
            return bundle.keySet();
        }
    }
}
