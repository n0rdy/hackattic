package me.n0rdy.hackattic.task.reading_qr;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.net.URL;
import java.util.Optional;

@Component
public class QRDecoder {
    private static final Logger logger = LoggerFactory.getLogger(QRDecoder.class);

    @SneakyThrows
    public Optional<String> decode(URL qrImageUri) {
        LuminanceSource source = new BufferedImageLuminanceSource(ImageIO.read(qrImageUri));
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return Optional.ofNullable(result.getText());
        } catch (NotFoundException e) {
            logger.error("QR code doesn't exist in provided image");
            return Optional.empty();
        }
    }
}
