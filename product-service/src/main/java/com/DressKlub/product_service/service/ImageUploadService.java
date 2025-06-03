package com.DressKlub.product_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ImageUploadService {

    @Autowired
    private Cloudinary cloudinary;

    public List<String> uploadImages(List<MultipartFile> files) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = (String) uploadResult.get("secure_url");
            imageUrls.add(url);
        }

        return imageUrls;
    }
}
