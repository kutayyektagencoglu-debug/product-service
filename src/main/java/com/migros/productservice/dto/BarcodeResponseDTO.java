package com.migros.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BarcodeResponseDTO {
    private Long barcodeId;
    private String barcodeCode;
    private String barcodeType;
    private String productCode;
}
