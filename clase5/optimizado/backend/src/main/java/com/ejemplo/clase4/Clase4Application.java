package com.ejemplo.clase4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Clase4Application {
    public static void main(String[] args) {
        SpringApplication.run(Clase4Application.class, args);
    }
}
