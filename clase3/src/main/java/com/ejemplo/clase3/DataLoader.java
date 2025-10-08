package com.ejemplo.clase3;

import com.ejemplo.clase3.model.Producto;
import com.ejemplo.clase3.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductoRepository productoRepository;

    public DataLoader(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) {
        if (productoRepository.count() == 0) {
            productoRepository.save(new Producto("Manzana", 1.5));
            productoRepository.save(new Producto("Banana", 2.0));
            productoRepository.save(new Producto("Naranja", 1.8));
        }
    }
}
