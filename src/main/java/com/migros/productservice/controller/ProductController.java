package com.migros.productservice.controller;

import com.migros.productservice.dto.ProductRequestDTO;
import com.migros.productservice.dto.ProductResponseDTO;
import com.migros.productservice.enums.UnitType;
import com.migros.productservice.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-service")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {

        this.productService = productService;
    }

    // CREATE
    @PostMapping
    public ProductResponseDTO createProduct(@RequestBody ProductRequestDTO dto) {

        return productService.createProduct(dto);
    }

    // READ ALL
    @GetMapping
    public List<ProductResponseDTO> getAllCategories() {

        return productService.getAllCategories();
    }
    //READ BY ID
    @GetMapping("/{id}")
    public  ProductResponseDTO getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
    // READ BY NAME
    @GetMapping("/{name}")
    public ProductResponseDTO getProductByName(@PathVariable String name) {

        return productService.getProductByName(name);
    }

    //READ BY CODE
    @GetMapping("/{code}")
    public ProductResponseDTO getProductByCode(@PathVariable String code){

        return productService.getProductByCode(code);
    }

    //READ BY UNIT
    @GetMapping("/{unit}")
    public List<ProductResponseDTO> getProductByUnit(@PathVariable UnitType unit){

        return productService.getProductByUnit(unit);
    }

    //READ BY CATEGORY CODE
    @GetMapping("/{categoryCode}")
    public List<ProductResponseDTO> getProductByCategoryCode(@PathVariable String categoryCode){

        return productService.getProductByCategoryCode(categoryCode);
    }

    //READ BY BRAND
    @GetMapping("/{brand}")
    public List<ProductResponseDTO> getProductByBrand(@PathVariable String brand){

        return productService.getProductByBrand(brand);
    }

    // UPDATE
    @PutMapping("/{name}")
    public ProductResponseDTO updateProduct(@PathVariable String name, @RequestBody ProductRequestDTO updatedDTO) {

        return productService.updateProduct(name, updatedDTO);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id) {

        productService.deleteProductById(id);
    }

    @DeleteMapping("/{name}")
    public void deleteProductByName(@PathVariable String name) {

        productService.deleteProductByName(name);
    }

    @DeleteMapping("/{code}")
    public void deleteProductByCode(@PathVariable String code) {

        productService.deleteProductByCode(code);
    }
}
