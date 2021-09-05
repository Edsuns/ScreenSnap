package io.github.edsuns.screensnap.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Edsuns@qq.com on 2020-05-29
 */
public final class QrCodeUtil {

    /**
     * 解析二维码等
     *
     * @param image 源
     * @return null即没有二维码
     */
    public static Result parseQrCode(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        if (width < 10 || height < 10) return null;

        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

            return new MultiFormatReader().decode(binaryBitmap, hints);
        } catch (NotFoundException e) {
            return null;
        }
    }
}
