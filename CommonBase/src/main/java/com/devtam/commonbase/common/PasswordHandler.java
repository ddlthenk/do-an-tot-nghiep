package com.devtam.commonbase.common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHandler {
    static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    BCryptPasswordEncoder getInstance() {
        return passwordEncoder;
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }

    public static String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
