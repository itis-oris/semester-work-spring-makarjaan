package com.makarova.utils;

import com.cloudinary.Cloudinary;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;
import jakarta.mail.MessagingException;
import jakarta.mail.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CloudinaryUtil {

    @Autowired
    private Cloudinary cloudinary;

    public String upload(File file, String filename) throws IOException {
        Map<String, Object> uploadParams = Map.of("public_id", filename);
        Map uploadResult = cloudinary.uploader().upload(file, uploadParams);
        return (String) uploadResult.get("secure_url");
    }

    public File makeFile(Part part, String filename) throws IOException {
        File tempFile = File.createTempFile("profile_", filename);

        try (InputStream content = part.getInputStream();
             OutputStream out = Files.newOutputStream(tempFile.toPath())) {
            byte[] buffer = new byte[1024];
            int count;
            while ((count = content.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return tempFile;
    }

}