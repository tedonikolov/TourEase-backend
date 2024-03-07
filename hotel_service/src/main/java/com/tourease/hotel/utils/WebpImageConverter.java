package com.tourease.hotel.utils;

import com.luciad.imageio.webp.WebPWriteParam;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

@Component
public class WebpImageConverter {

    public static byte[] convertToWebP(BufferedImage inputImage) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Create a WebP image writer and set its parameters
        ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
        ImageWriteParam param = new WebPWriteParam(Locale.getDefault());
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

        // Write the WebP image to the output stream
        writer.setOutput(ImageIO.createImageOutputStream(outputStream));
        writer.write(null, new IIOImage(inputImage, null, null), param);

        // Clean up resources
        writer.dispose();

        return outputStream.toByteArray();
    }

}
