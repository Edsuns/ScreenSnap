package com.s0n1.screensnap.widget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Edsuns@qq.com on 2020-06-06
 */
public class UrlLabel extends JLabel {
    private MouseAdapter mouseAdapter;

    public UrlLabel(String url) {
        this(url, url);
    }

    public UrlLabel(String text, String url) {
        setText(text);
        setUrl(url);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void setUrl(String url) {
        if (mouseAdapter != null) removeMouseListener(mouseAdapter);
        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        };
        addMouseListener(mouseAdapter);
    }

    @Override
    public void setText(String text) {
        super.setText("<html><a href=''>" + text + "</a></html>");
    }
}
