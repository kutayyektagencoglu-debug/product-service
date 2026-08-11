package com.migros.productservice.mapper;

import com.migros.productservice.Model.Product;
import com.migros.productservice.dto.ProductRequestDTO;
import com.migros.productservice.dto.ProductResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDTO toResponseDTO(Product product);
    Product toEntity(ProductRequestDTO requestDTO);
    List<ProductResponseDTO> toResponseDTOList(List<Product> products);
}
