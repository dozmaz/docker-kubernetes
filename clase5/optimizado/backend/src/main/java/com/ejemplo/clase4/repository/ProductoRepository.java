package com.ejemplo.clase4.repository;

import com.ejemplo.clase4.model.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductoRepository extends MongoRepository<Producto, String> {
}

