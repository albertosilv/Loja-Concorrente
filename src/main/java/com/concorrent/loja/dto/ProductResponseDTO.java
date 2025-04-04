package com.concorrent.loja.dto;


import com.concorrent.loja.model.Product;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import java.time.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponseDTO {

    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
    private String descricao;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCriacao;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataAtualizacao;

    public ProductResponseDTO(Product product) {
    }

    // Campos calculados ou derivados
    @JsonProperty("valor_total_estoque")
    public Double getValorTotalEstoque() {
        return price * quantity;
    }

}