package com.example.pfkworkspace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableCaching
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class PfkworkspaceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PfkworkspaceApplication.class, args);
    }
}
