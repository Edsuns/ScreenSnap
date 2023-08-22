import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

import java.util.ArrayList;
import java.util.List;

public class JnaGetScreenRectangles {

    public static void main(String[] args) {
        List<WinUser.RECT> screenRectangles = getAllScreenRectangles();
        for (int i = 0; i < screenRectangles.size(); i++) {
            WinUser.RECT rect = screenRectangles.get(i);
            System.out.println("Screen " + i + " Rectangle: " + rect.left + ", " + rect.top + ", " + rect.right + ", " + rect.bottom);
        }
    }

    public static List<WinUser.RECT> getAllScreenRectangles() {
        List<WinUser.RECT> screenRectangles = new ArrayList<>();

        User32.INSTANCE.EnumDisplayMonitors(null, null, (hMonitor, hdc, rect, lparam) -> {
            WinUser.MONITORINFO monitorinfo = new WinUser.MONITORINFO();
            WinDef.BOOL bool = User32.INSTANCE.GetMonitorInfo(hMonitor, monitorinfo);
            if (bool.booleanValue()) {
                screenRectangles.add(monitorinfo.rcMonitor);
            }
            return 1;
        }, new WinDef.LPARAM(0));

        return screenRectangles;
    }

}
