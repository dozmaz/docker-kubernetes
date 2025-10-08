package com.ejemplo.clase3.repository;

import com.ejemplo.clase3.model.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductoRepository extends MongoRepository<Producto, String> {
}

