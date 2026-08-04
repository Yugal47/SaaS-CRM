package com.crm.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// scanBasePackages pulls in com.crm.common (JwtAuthFilter, GlobalExceptionHandler, etc.)
// since it lives outside this service's own package tree
@SpringBootApplication(scanBasePackages = {"com.crm.user", "com.crm.common"})
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
