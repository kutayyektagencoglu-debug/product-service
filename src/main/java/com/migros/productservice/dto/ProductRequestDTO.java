package com.migros.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.migros.productservice.enums.UnitType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductRequestDTO {
    @JsonProperty("productName")
    @NotBlank
    private String name;
    @NotBlank
    private String brand;
    @NotNull
    private UnitType unit;
    @NotBlank
    @Size(min = 2, max = 2)
    private String categoryCode;
}
