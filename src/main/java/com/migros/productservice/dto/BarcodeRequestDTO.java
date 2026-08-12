package com.migros.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BarcodeRequestDTO {
    @NotBlank
    private String productCode;
    @NotBlank
    private String unit;
}
