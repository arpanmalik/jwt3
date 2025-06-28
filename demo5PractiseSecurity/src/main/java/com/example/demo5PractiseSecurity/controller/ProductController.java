package com.example.demo5PractiseSecurity.controller;

import com.example.demo5PractiseSecurity.models.Product;
import com.example.demo5PractiseSecurity.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("add-product")
    public Object addProduct(@RequestBody Product product, Authentication authentication){
        return productService.addProduct(product, authentication.getName());
    }

    @GetMapping("products")
    public Object getProducts(Principal principal){
        return productService.getAllProducts(principal.getName());
    }

    @PutMapping("product/{id}")
    public Object updateProduct(@RequestBody Product product, Authentication authentication, @PathVariable int id){
        String username = authentication.getName();
        return productService.updateProduct(product, id, username);
    }

    @GetMapping("product/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable int id, Authentication authentication){
        String username = authentication.getName();
        return productService.getProductById(id, username);
    }
    @DeleteMapping("product/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id, Authentication authentication){
        String username = authentication.getName();
        return productService.delete(id, username);
    }
    @GetMapping("product/{value}")
    public Object getProductByUser(@PathVariable String value){
        return productService.getProductByUser(value);
    }
}
