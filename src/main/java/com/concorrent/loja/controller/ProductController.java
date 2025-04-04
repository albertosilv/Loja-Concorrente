package com.concorrent.loja.controller;

import com.concorrent.loja.dto.*;
import com.concorrent.loja.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductRequestDTO productDTO) {
        ProductResponseDTO createdProduct = productService.create(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        List<ProductResponseDTO> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
        try {
            ProductResponseDTO product = productService.findById(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable Long id,
            @RequestBody ProductRequestDTO productDTO) {
        try {
            ProductResponseDTO updatedProduct = productService.update(id, productDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseProduct(@RequestBody PurchaseRequestDTO purchaseRequest) {
        try {
            ProductResponseDTO product = productService.purchaseProduct(purchaseRequest);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/report")
    public ResponseEntity<SalesReportDTO> generateSalesReport() {
        SalesReportDTO report = productService.generateSalesReport();
        return ResponseEntity.ok(report);
    }
}
