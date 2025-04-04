package com.concorrent.loja.dto;


import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class SalesReportDTO {
    private int totalSales;
    private List<SoldProductDTO> products;
}
