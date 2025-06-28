package com.example.demo5PractiseSecurity.services;

import com.example.demo5PractiseSecurity.models.Product;
import com.example.demo5PractiseSecurity.models.User;
import com.example.demo5PractiseSecurity.repo.ProductRepo;
import com.example.demo5PractiseSecurity.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private UserRepo repo;
    @Autowired
    private ProductRepo productRepo;

    public Object addProduct(Product product, String username) {
        User user = repo.findByUsername(username);
        if(user==null){
            return new UsernameNotFoundException("User not found");
        }
        product.setCreatedBy(user);
        Product saved = productRepo.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    public Object getAllProducts(String username) {
        User user = repo.findByUsername(username);
        if(user==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
        if(user.getRole().equals("ADMIN")){
            List<Product> products = productRepo.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(products);
        }
        List<Product> products = productRepo.findByCreatedBy(user);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    public Object updateProduct(Product product, int id, String username) {
        User user = repo.findByUsername(username);
        if(user==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
        Optional<Product> existingProduct = productRepo.findById(id);
        if(existingProduct.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        Product exist = existingProduct.get();
        if(exist.getCreatedBy()==null || exist.getCreatedBy().getId()!=user.getId()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        exist.setName(product.getName());
        exist.setDescription(product.getDescription());
        exist.setPrice(product.getPrice());
        Product updated = productRepo.save(exist);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    public ResponseEntity<Object> getProductById(int id, String username) {
        Optional<Product> product = productRepo.findById(id);
        if(product.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(product.get().getCreatedBy()==null ||  !product.get().getCreatedBy().getUsername().equals(username)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(product.get());
    }

    public ResponseEntity<Object> delete(int id, String username) {
        User user = repo.findByUsername(username);
        if(user==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<Product> product = productRepo.findById(id);
        if(product.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Product fetchedProduct = product.get();
        if(fetchedProduct.getCreatedBy().getId()!=user.getId()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        productRepo.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted successfully");
    }

    public Object getProductByUser(String username) {
        User user = repo.findByUsername(username);
        if(user==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        List<Product> products = productRepo.findByCreatedBy(user);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }
}
