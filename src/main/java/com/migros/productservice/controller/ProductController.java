package com.migros.productservice.controller;

import com.migros.productservice.dto.ProductRequestDTO;
import com.migros.productservice.dto.ProductResponseDTO;
import com.migros.productservice.enums.UnitType;
import com.migros.productservice.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
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
    public List<ProductResponseDTO> getAllProducts() {

        return productService.getAllProducts();
    }
    //READ BY ID
    @GetMapping("/id/{id}")
    public  ProductResponseDTO getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
    // READ BY NAME
    @GetMapping("/name/{name}")
    public ProductResponseDTO getProductByName(@PathVariable String name) {

        return productService.getProductByName(name);
    }

    //READ BY CODE
    @GetMapping("/code/{code}")
    public ProductResponseDTO getProductByCode(@PathVariable String code){

        return productService.getProductByCode(code);
    }

    //READ BY UNIT
    @GetMapping("/unit/{unit}")
    public List<ProductResponseDTO> getProductByUnit(@PathVariable UnitType unit){

        return productService.getProductByUnit(unit);
    }

    //READ BY CATEGORY CODE
    @GetMapping("/categoryCode/{categoryCode}")
    public List<ProductResponseDTO> getProductByCategoryCode(@PathVariable String categoryCode){

        return productService.getProductByCategoryCode(categoryCode);
    }

    //READ BY BRAND
    @GetMapping("/brand/{brand}")
    public List<ProductResponseDTO> getProductByBrand(@PathVariable String brand){

        return productService.getProductByBrand(brand);
    }

    // UPDATE
    @PutMapping("/id/{id}")
    public ProductResponseDTO updateProductById(@PathVariable Long id, @RequestBody ProductRequestDTO updatedDTO) {

        return productService.updateProductById(id, updatedDTO);
    }

    @PutMapping("/name/{name}")
    public ProductResponseDTO updateProductByName(@PathVariable String name, @RequestBody ProductRequestDTO updatedDTO) {

        return productService.updateProductByName(name, updatedDTO);
    }

    @PutMapping("/code/{code}")
    public ProductResponseDTO updateProductByCode(@PathVariable String code, @RequestBody ProductRequestDTO updatedDTO) {

        return productService.updateProductByCode(code, updatedDTO);
    }

    // DELETE
    @DeleteMapping("/{code}")
    public void deleteProductByCode(@PathVariable String code) {

        productService.deleteProductByCode(code);
    }
}
