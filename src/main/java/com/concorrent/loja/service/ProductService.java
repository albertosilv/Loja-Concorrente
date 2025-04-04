package com.concorrent.loja.service;

import com.concorrent.loja.dto.ProductRequestDTO;
import com.concorrent.loja.dto.ProductResponseDTO;
import com.concorrent.loja.dto.PurchaseRequestDTO;
import com.concorrent.loja.dto.SalesReportDTO;

import java.util.List;

public interface ProductService {

    ProductResponseDTO create(ProductRequestDTO productDTO);

    List<ProductResponseDTO> findAll();

    ProductResponseDTO findById(Long id) throws Exception;

    ProductResponseDTO update(Long id, ProductRequestDTO productDTO) throws Exception;

    ProductResponseDTO purchaseProduct(PurchaseRequestDTO purchaseRequest) throws Exception;

    SalesReportDTO generateSalesReport();
}
