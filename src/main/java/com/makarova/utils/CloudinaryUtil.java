package com.makarova.utils;

import com.cloudinary.Cloudinary;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CloudinaryUtil {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryUtil(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String upload(File file, String filename) throws IOException {
        Map<String, Object> uploadParams = Map.of("public_id", filename);
        Map uploadResult = cloudinary.uploader().upload(file, uploadParams);
        return (String) uploadResult.get("secure_url");
    }
}
