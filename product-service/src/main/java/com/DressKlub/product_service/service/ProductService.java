package com.DressKlub.product_service.service;

import com.DressKlub.product_service.dto.ProductDTO;
import com.DressKlub.product_service.model.Product;
import com.DressKlub.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository repository, ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));
    }


    public Product createProduct(ProductDTO dto) {
        Product product = new Product();

        product.setName(dto.getName());
        product.setSize(dto.getSize());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        product.setImageUrl(dto.getImageUrl());

        return productRepository.save(product);
    }



    public Product updateProduct(Long id, ProductDTO productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setName(productDetails.getName());
        product.setSize(productDetails.getSize());
        product.setPrice(productDetails.getPrice());
        product.setImageUrl(productDetails.getImageUrl());
        product.setCategory(productDetails.getCategory());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> searchBy(String name, String category) {
        if ("all".equalsIgnoreCase(name) || "all".equalsIgnoreCase(category)) return productRepository.findAll();
        if (name != null) return productRepository.findByNameContainingIgnorCase(name);
        if (category != null) return productRepository.findByCategoryContainingIgnorCase(category);

        return Collections.emptyList();
    }
}

