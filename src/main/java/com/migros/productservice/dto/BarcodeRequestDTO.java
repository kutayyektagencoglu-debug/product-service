package com.migros.productservice.dto;

import com.migros.productservice.enums.UnitType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BarcodeRequestDTO {
    private String productCode;
    @Enumerated(EnumType.STRING)
    private UnitType unit;
}
