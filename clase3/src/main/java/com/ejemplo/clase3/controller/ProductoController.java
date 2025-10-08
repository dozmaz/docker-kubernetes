package com.ejemplo.clase3.controller;

import com.ejemplo.clase3.model.Producto;
import com.ejemplo.clase3.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public List<Producto> getAll() {
        return productoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Producto> getById(@PathVariable String id) {
        return productoRepository.findById(id);
    }

    @PostMapping
    public Producto create(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    @PutMapping("/{id}")
    public Producto update(@PathVariable String id, @RequestBody Producto producto) {
        producto.setId(id);
        return productoRepository.save(producto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        productoRepository.deleteById(id);
    }
}

