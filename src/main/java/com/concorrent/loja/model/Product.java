package com.concorrent.loja.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(nullable = false, length = 100)
    @JsonProperty("name")
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    @JsonProperty("price")
    private BigDecimal price;

    @Column(nullable = false)
    @JsonProperty("quantity")
    private int quantity;

    @Version
    private Long version;
}
