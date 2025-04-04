package com.concorrent.loja.repository;

import com.concorrent.loja.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
