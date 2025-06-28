package com.example.demo5PractiseSecurity.controller;

import com.example.demo5PractiseSecurity.models.User;
import com.example.demo5PractiseSecurity.services.JwtService;
import com.example.demo5PractiseSecurity.services.JwtUtil;
import com.example.demo5PractiseSecurity.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtService;

    @GetMapping("home")
    public String home(){
        System.out.println("Hello World!");
        return "Home Page";
    }
    @GetMapping("admin")
    public String admin(){
        return "Welcome to Admin Page";
    }

    @GetMapping("check")
    public String faltu(){
        return "Restricted Page";
    }

    @PostMapping("register")
    public Object register(@RequestBody User user){
        return userService.register(user);
    }

    @PostMapping("login")
    public Object login(@RequestBody User user){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        if(authentication.isAuthenticated()){
            String token = jwtService.generateToken(user.getUsername());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
