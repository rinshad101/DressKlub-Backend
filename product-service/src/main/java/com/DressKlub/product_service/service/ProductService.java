package com.DressKlub.product_service.service;

import com.DressKlub.product_service.model.Product;
import com.DressKlub.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return repository.findById(id);
    }

    public Product addProduct(Product product) {
        return repository.save(product);
    }


    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = repository.findById(id).orElseThrow();
        product.setName(productDetails.getName());
        product.setSize(productDetails.getSize());
        product.setPrice(productDetails.getPrice());
        product.setImageUrl(productDetails.getImageUrl());
        product.setCategory(productDetails.getCategory());
        return repository.save(product);
    }
}

