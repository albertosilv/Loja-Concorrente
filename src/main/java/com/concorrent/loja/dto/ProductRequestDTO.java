package com.concorrent.loja.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String name;

    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser positivo")
    @Digits(integer = 10, fraction = 2, message = "O preço deve ter no máximo 2 casas decimais")
    private Double price;

    @NotNull(message = "A quantidade é obrigatória")
    @PositiveOrZero(message = "A quantidade não pode ser negativa")
    private Integer quantity;

    // Campos opcionais podem ser adicionados aqui
    private String descricao;
}