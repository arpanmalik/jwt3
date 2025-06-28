package com.example.demo5PractiseSecurity.repo;

import com.example.demo5PractiseSecurity.models.Product;
import com.example.demo5PractiseSecurity.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
    List<Product> findByCreatedBy(User user);
}
