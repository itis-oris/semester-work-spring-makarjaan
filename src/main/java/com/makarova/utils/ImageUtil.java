package com.makarova.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {

    public static File makeTempFile(MultipartFile multipartFile) throws IOException {
        File file = File.createTempFile("upload-", ".jpg");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;
    }
}