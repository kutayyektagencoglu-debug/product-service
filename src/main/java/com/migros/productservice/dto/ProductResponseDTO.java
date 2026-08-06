package com.migros.productservice.dto;

import com.migros.productservice.enums.UnitType;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class ProductResponseDTO {
    private Long id;
    private String name;
    @Size(min = 5, max = 5)
    private String code;
    private String brand;
    private UnitType unit;
    @Size(min = 2, max = 2)
    private String categoryCode;
}
