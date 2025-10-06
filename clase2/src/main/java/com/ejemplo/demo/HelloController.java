package com.ejemplo.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hola desde /hello";
    }

    @GetMapping("/saludo")
    public String saludo() {
        return "¡Saludos desde /saludo!";
    }
}

