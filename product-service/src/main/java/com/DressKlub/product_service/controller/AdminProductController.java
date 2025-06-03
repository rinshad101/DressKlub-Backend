package com.DressKlub.product_service.controller;

import com.DressKlub.product_service.dto.ProductDTO;
import com.DressKlub.product_service.model.Product;
import com.DressKlub.product_service.service.ImageUploadService;
import com.DressKlub.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController{

    @Autowired
    private ImageUploadService imageUploadService;

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDTO productDTO){
        Product createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.status(201).body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO){
        productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);;
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadImages(@RequestParam("files") List<MultipartFile> files) {
        try {
            List<String> urls = imageUploadService.uploadImages(files);
            return ResponseEntity.ok(urls);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
}
