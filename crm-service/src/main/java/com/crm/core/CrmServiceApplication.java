package com.crm.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.crm.core", "com.crm.common"})
public class CrmServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrmServiceApplication.class, args);
    }
}
