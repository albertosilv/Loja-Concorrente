package com.concorrent.loja.dto;


import com.concorrent.loja.model.Product;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponseDTO {

    private Long id;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private String descricao;

    public ProductResponseDTO(Product product) {

    }

}