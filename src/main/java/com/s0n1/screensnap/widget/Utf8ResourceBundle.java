package com.s0n1.screensnap.widget;

import com.s0n1.screensnap.util.FrameUtil;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * UTF-8 friendly ResourceBundle support
 * <p>
 * Utility that allows having multi-byte characters inside java .property files.
 * It removes the need for Sun's native2ascii application, you can simply have
 * UTF-8 encoded editable .property files.
 * 用于解决JRE1.8及更低版本获取ResourceBundle编码不是UTF-8的问题
 * <p>
 * Use:
 * ResourceBundle bundle = Utf8ResourceBundle.getBundle("bundle_name");
 *
 * @author Tomas Varaneckas <tomas.varaneckas@gmail.com>
 * Modified by Edsuns@qq.com on 6/29/2020.
 */
public abstract class Utf8ResourceBundle {

    /**
     * Gets the unicode friendly resource bundle
     *
     * @param baseName
     * @return Unicode friendly resource bundle
     * @see ResourceBundle#getBundle(String)
     */
    public static ResourceBundle getBundle(final String baseName) {
        return createUtf8ResourceBundle(
                ResourceBundle.getBundle(baseName));
    }

    public static ResourceBundle getBundle(final String baseName,
                                           final Locale locale) {
        return createUtf8ResourceBundle(
                ResourceBundle.getBundle(baseName, locale));
    }

    /**
     * Creates unicode friendly {@link ResourceBundle} if possible.
     *
     * @param bundle
     * @return Unicode friendly property resource bundle
     */
    private static ResourceBundle createUtf8ResourceBundle(
            final ResourceBundle bundle) {
        if (!FrameUtil.isOldVersionJava) {
            return bundle;// Java9及更高版本不需要处理
        }
        return new Utf8PropertyResourceBundle(bundle);
    }

    /**
     * Resource Bundle that does the hard work
     */
    private static class Utf8PropertyResourceBundle extends ResourceBundle {

        /**
         * Bundle with unicode data
         */
        private final ResourceBundle bundle;

        /**
         * Initializing constructor
         *
         * @param bundle
         */
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
            return new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        }

        @Override
        public Locale getLocale() {
            return bundle.getLocale();
        }
    }
}
