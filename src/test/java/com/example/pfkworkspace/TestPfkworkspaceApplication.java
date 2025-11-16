package com.example.pfkworkspace;

import org.springframework.boot.SpringApplication;

public class TestPfkworkspaceApplication {

    public static void main(String[] args) {
        SpringApplication.from(PfkworkspaceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
