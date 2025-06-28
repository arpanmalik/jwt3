package com.example.demo5PractiseSecurity.services;

import com.example.demo5PractiseSecurity.models.User;
import com.example.demo5PractiseSecurity.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo repo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Object register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = repo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}
