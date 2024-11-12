package com.devtam.commonbase.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class CloudinaryService {


    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;
    private Cloudinary cloudinaryConfig;

    private final Logger _log = LogManager.getLogger(CloudinaryService.class);

    public CloudinaryService() {
        this.cloudinaryConfig = cloudinaryConfig();
    }

    public Cloudinary cloudinaryConfig() {
        Cloudinary cloudinary = null;
        Map<String, String> config = new HashMap<String, String>();
        config.put("cloud_name", "dqvhfr35a");
        config.put("api_key", "565243637324851");
        config.put("api_secret", "gzrMLn9GXtI97oqIe2gj4rXs7Sw");
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }

    public String uploadFile(MultipartFile file) {
        try {
            File uploadedFile = convertMultiPartToFile(file);
            Map<String, Object> options = ObjectUtils.asMap("folder", "DoAnFile");
            Map uploadResult = cloudinaryConfig.uploader().upload(uploadedFile, options);
            uploadedFile.delete();
            return uploadResult.get("url").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteFile(String url) {
        try {
            int lastIndex = url.lastIndexOf("/");
            String filename = url.substring(lastIndex + 1);
            int lastDotIndex = filename.lastIndexOf(".");
            String publicId = "DoAnFile/" + filename.substring(0, lastDotIndex);
            Map<String, Object> options = ObjectUtils.emptyMap();
            Map uploadResult = cloudinaryConfig.uploader().destroy(publicId, options);
            if (uploadResult.get("result").toString().equalsIgnoreCase("ok")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            _log.error(e.getMessage());
            return false;
        }
    }


    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
