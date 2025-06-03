package com.DressKlub.product_service.config;


import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "der6rgv5r");
        config.put("api_key", "917619594843927");
        config.put("api_secret", "D8YvpfsbSI_Q-NlDBAJ6HAzM3eI");
        return new Cloudinary(config);
    }
}
