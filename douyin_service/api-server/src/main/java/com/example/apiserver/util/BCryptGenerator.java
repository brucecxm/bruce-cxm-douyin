package com.example.apiserver.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "test";
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println("Original password: " + password);
        System.out.println("Encoded password: " + encodedPassword);
        
        // Test the password match
        boolean matches = passwordEncoder.matches(password, encodedPassword);
        System.out.println("Password matches: " + matches);
    }
}