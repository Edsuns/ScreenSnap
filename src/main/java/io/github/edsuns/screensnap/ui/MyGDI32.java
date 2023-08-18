package io.github.edsuns.screensnap.ui;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.W32APIOptions;

public interface MyGDI32 extends com.sun.jna.platform.win32.GDI32 {
    MyGDI32 INSTANCE = Native.load("gdi32", MyGDI32.class, W32APIOptions.DEFAULT_OPTIONS);

    int GetPixel(WinDef.HDC hdc, int x, int y);
}