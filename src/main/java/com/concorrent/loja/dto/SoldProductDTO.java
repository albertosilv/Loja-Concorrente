package com.concorrent.loja.dto;

import jdk.jshell.Snippet;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SoldProductDTO {
    private Long id;
    private String name;
    private int quantitySold;

}
