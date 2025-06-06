package com.makarova.utils;

//import com.cloudinary.Cloudinary;
//import java.io.File;
//import java.io.IOException;
//import java.util.Map;
//
//import com.cloudinary.Cloudinary;
//import org.springframework.stereotype.Component;
//
//import jakarta.annotation.PostConstruct;
//import java.io.File;
//import java.io.IOException;
//import java.util.Map;
//
//@Component
//public class CloudinaryUtil {
//
//    private final Cloudinary cloudinary;
//
//    public CloudinaryUtil(Cloudinary cloudinary) {
//        this.cloudinary = cloudinary;
//    }
//
//    // Загрузка файла
//    public String upload(File file, String folder) throws IOException {
//        Map<String, Object> params = Map.of(
//                "folder", folder,
//                "use_filename", true,
//                "unique_filename", false
//        );
//        Map result = cloudinary.uploader().upload(file, params);
//        return (String) result.get("secure_url");
//    }
//
//    // Пример: загрузка файла с указанием имени
//    public String uploadWithPublicId(File file, String publicId, String folder) throws IOException {
//        Map<String, Object> params = Map.of(
//                "folder", folder,
//                "public_id", publicId,
//                "overwrite", true,
//                "invalidate", true
//        );
//        Map result = cloudinary.uploader().upload(file, params);
//        return (String) result.get("secure_url");
//    }
//}