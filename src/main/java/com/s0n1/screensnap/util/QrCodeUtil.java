package com.s0n1.screensnap.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public final class QrCodeUtil {

    public static Result parseQrCode(BufferedImage image) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

            return new MultiFormatReader().decode(binaryBitmap, hints);
        } catch (NotFoundException e) {
            System.out.println("There isn't any QRCode.");
        }
        return null;
    }
}
