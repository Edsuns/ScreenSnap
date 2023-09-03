package com.github.meimingle.util;

import io.github.edsuns.screensnap.util.Util;

public class Test {
    public static void main(String[] args) {

        System.out.println(Util.getFullVirtualScreenRectangle());
        System.out.println(Util.getColorFromScreenPixel(-128,1000));
    }
}
