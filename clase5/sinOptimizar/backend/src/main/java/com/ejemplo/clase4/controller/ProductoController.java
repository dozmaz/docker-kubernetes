package com.ejemplo.clase4.controller;

import com.ejemplo.clase4.model.Producto;
import com.ejemplo.clase4.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.web.bind.annotation.*;
import org.springframework.cache.annotation.Cacheable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    @Autowired
    private ProductoRepository productoRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    @GetMapping
    @Cacheable("productos")
    public List<Producto> getAll() {
        Object newkey = org.springframework.cache.interceptor.SimpleKeyGenerator.generateKey(new Object[]{});
        boolean isCacheHit = org.springframework.cache.interceptor.SimpleKey.EMPTY.equals(newkey);
        if (isCacheHit) {
            logger.info("Cache HIT: "+ org.springframework.cache.interceptor.SimpleKey.EMPTY.toString());
        } else {
            logger.info("Cache MISS: "+newkey.toString());
        }
        logger.info("Obteniendo todos los productos de la base de datos");

        return productoRepository.findAll();
    }

    @GetMapping("/{id}")
    @Cacheable(value = "producto", key = "#id")
    public Optional<Producto> getById(@PathVariable String id) {
        logger.info("Obteniendo usuario de la base de datos para id: {}", id);
        return productoRepository.findById(id);
    }

    @PostMapping
    @CacheEvict(value = {"productos", "producto"}, allEntries = true) // Limpiar caché al crear un nuevo producto
    public Producto create(@RequestBody Producto producto) {
        logger.info("Creando un nuevo producto: {}", producto.getNombre());
        return productoRepository.save(producto);
    }

    @PutMapping("/{id}")
    @CacheEvict(value = {"productos", "producto"}, allEntries = true) // Limpiar caché al actualizar un producto
    public Producto update(@PathVariable String id, @RequestBody Producto producto) {
        logger.info("Actualizando el producto con id: {}", id);
        producto.setId(id);
        return productoRepository.save(producto);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = {"productos", "producto"}, allEntries = true) // Limpiar caché al eliminar un producto
    public void delete(@PathVariable String id) {
        logger.info("Eliminando el producto con id: {}", id);
        productoRepository.deleteById(id);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity
                .ok()
                .header("Content-Type", "text/plain")
                .body("Backend OK\n");
    }
}
