package com.DressKlub.product_service.controller;

import com.DressKlub.product_service.model.Product;
import com.DressKlub.product_service.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    public ResponseEntity<List<Product>> searchBy(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category
    ){
        List<Product> products = productService.searchBy(name,category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


}
