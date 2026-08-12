package com.migros.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BarcodeResponseDTO {
    @NotNull
    private Long barcodeId;
    @NotBlank
    private String barcodeCode;
    @NotBlank
    private String barcodeType;
    @NotBlank
    private String productCode;
}
