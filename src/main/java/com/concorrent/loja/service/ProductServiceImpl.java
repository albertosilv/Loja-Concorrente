package com.concorrent.loja.service;

import com.concorrent.loja.dto.*;
import com.concorrent.loja.model.Product;
import com.concorrent.loja.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    private final Map<Long, Integer> salesRecords = new ConcurrentHashMap<>();

    private final Map<Long, ReentrantLock> productLocks = new WeakHashMap<>();

    private ReentrantLock getLockForProduct(Long productId) {
        synchronized (productLocks) {
            return productLocks.computeIfAbsent(productId, id -> new ReentrantLock(true)); // justo
        }
    }

    @Override
    public ProductResponseDTO create(ProductRequestDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        productRepository.save(product);
        return modelMapper.map(product, ProductResponseDTO.class);
    }

    @Override
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll().stream()
                .map(product -> modelMapper.map(product, ProductResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDTO findById(Long id) throws Exception {
        Product product = productRepository.findById(id).orElseThrow(Exception::new);
        return modelMapper.map(product, ProductResponseDTO.class);
    }

    @Override
    public ProductResponseDTO update(Long id, ProductRequestDTO productDTO) throws Exception {
        ReentrantLock lock = getLockForProduct(id);
        lock.lock();
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new Exception("Produto não encontrado"));

            product.setQuantity(productDTO.getQuantity());

            try {
                productRepository.save(product);
            } catch (OptimisticLockingFailureException e) {
                throw new Exception("Falha de concorrência: o produto foi alterado por outra requisição.");
            }

            return modelMapper.map(product, ProductResponseDTO.class);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public ProductResponseDTO purchaseProduct(PurchaseRequestDTO purchaseRequest) throws Exception {
        ReentrantLock lock = getLockForProduct(purchaseRequest.getId());
        lock.lock();
        try {
            Product product = productRepository.findById(purchaseRequest.getId())
                    .orElseThrow(() -> new Exception("Produto não encontrado"));

            if (product.getQuantity() < purchaseRequest.getQuantity()) {
                throw new Exception("Quantidade insuficiente em estoque");
            }

            product.setQuantity(product.getQuantity() - purchaseRequest.getQuantity());

            try {
                productRepository.save(product);
            } catch (OptimisticLockingFailureException e) {
                throw new Exception("Falha de concorrência: o produto foi alterado por outra requisição.");
            }

            salesRecords.merge(purchaseRequest.getId(), purchaseRequest.getQuantity(), Integer::sum);

            return modelMapper.map(product, ProductResponseDTO.class);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public SalesReportDTO generateSalesReport() {
        List<SoldProductDTO> soldProducts = salesRecords.entrySet().stream()
                .map(entry -> {
                    Product product = productRepository.findById(entry.getKey()).orElse(null);
                    return product != null ?
                            SoldProductDTO.builder()
                                    .id(product.getId())
                                    .name(product.getName())
                                    .quantitySold(entry.getValue())
                                    .build() :
                            null;
                })
                .filter(soldProduct -> soldProduct != null)
                .collect(Collectors.toList());

        int totalSales = soldProducts.stream()
                .mapToInt(SoldProductDTO::getQuantitySold)
                .sum();

        return SalesReportDTO.builder()
                .totalSales(totalSales)
                .products(soldProducts)
                .build();
    }
}
