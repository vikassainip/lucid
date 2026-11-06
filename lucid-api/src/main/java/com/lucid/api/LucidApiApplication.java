package com.lucid.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.lucid.api", "com.lucid.core"})
@EntityScan(basePackages = "com.lucid.core")
@EnableJpaRepositories(basePackages = "com.lucid.core")
public class LucidApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(LucidApiApplication.class, args);
    }
}
