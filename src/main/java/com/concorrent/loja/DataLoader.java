package com.concorrent.loja;
import com.concorrent.loja.model.Product;
import com.concorrent.loja.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final ProductRepository productRepository;

    @PostConstruct
    public void init() {
        if (productRepository.count() == 0) {
            productRepository.save(Product.builder()
                    .name("Notebook")
                    .price(BigDecimal.valueOf(3500.00))
                    .quantity(100)
                    .build());

            productRepository.save(Product.builder()
                    .name("Mouse Gamer")
                    .price(BigDecimal.valueOf(250.00))
                    .quantity(200)
                    .build());

            productRepository.save(Product.builder()
                    .name("Teclado Mecânico")
                    .price(BigDecimal.valueOf(400.00))
                    .quantity(150)
                    .build());

            System.out.println("⚙️ Produtos iniciais inseridos na base!");
        }
    }
}

