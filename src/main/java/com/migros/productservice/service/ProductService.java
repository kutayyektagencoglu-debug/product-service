package com.migros.productservice.service;

import com.migros.productservice.Model.Product;
import com.migros.productservice.dto.ProductRequestDTO;
import com.migros.productservice.dto.ProductResponseDTO;
import com.migros.productservice.enums.UnitType;
import com.migros.productservice.mapper.ProductMapper;
import com.migros.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    public ProductService(ProductRepository productRepository, ProductMapper mapper) {
        this.mapper = mapper;
        this.productRepository = productRepository;
    }

    public void assignCode(Product product) {
        String categoryCode = product.getCategoryCode();
        //ASSIGN CATEGORY CODE
        int maxCategoryNumber = productRepository.findMaxCategoryNumber(categoryCode);
        int nextCategoryNumber = maxCategoryNumber + 1;
        product.setCategoryNumber(nextCategoryNumber);
        //ASSIGN CODE
        String code = categoryCode + nextCategoryNumber;
        product.setCode(code);
    }
    //CREATE
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Product product = mapper.toEntity(dto);
        if(productRepository.existsByName(product.getName())) {
            throw new IllegalArgumentException("Product name already exists");
        }
        //add method that checks whether categoryCode is valid
        assignCode(product);

        Product saved = productRepository.save(product);
        return mapper.toResponseDTO(saved);
    }

    //READ ALL
    public List<ProductResponseDTO> getAllCategories(){
        List<Product> products = productRepository.findAll();
        return mapper.toResponseDTOList(products);
    }
    //READ BY ID
    public ProductResponseDTO getProductById(Long id) {
        Product product= productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
        return mapper.toResponseDTO(product);
    }
    //READ BY NAME
    public ProductResponseDTO getProductByName(String name) {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + name));
        return mapper.toResponseDTO(product);
    }

    //READ BY CODE
    public ProductResponseDTO getProductByCode(String code){
        Product product = productRepository.findByCode(code)
                .orElseThrow(() ->  new IllegalArgumentException("Product not found: " + code));
        return mapper.toResponseDTO(product);
    }

    //READ BY UNIT
    public List<ProductResponseDTO> getProductByUnit(UnitType unit){
        List<Product> products = productRepository.findByUnit(unit);
        return mapper.toResponseDTOList(products);
    }

    //READ BY CATEGORY CODE
    public List<ProductResponseDTO> getProductByCategoryCode(String categoryCode){
        List<Product> products = productRepository.findByCategoryCode(categoryCode);
        return mapper.toResponseDTOList(products);
    }

    //READ BY BRAND
    public List<ProductResponseDTO> getProductByBrand(String brand){
        List<Product> products = productRepository.findByBrand(brand);
        return mapper.toResponseDTOList(products);
    }

    //UPDATE
    public void updateProduct(Product existingProduct, ProductRequestDTO desiredDTO) {
        //add method that checks whether categoryCode is valid
        existingProduct.setName(desiredDTO.getName());
        existingProduct.setUnit(desiredDTO.getUnit());
        existingProduct.setBrand(desiredDTO.getBrand());

        String categoryCode = desiredDTO.getCategoryCode();
        if(!categoryCode.equals(existingProduct.getCategoryCode())) {
            existingProduct.setCategoryCode(categoryCode);
            assignCode(existingProduct);
        }
    }

    public ProductResponseDTO updateProductById(Long id, ProductRequestDTO desiredDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        updateProduct(existingProduct, desiredDTO);

        Product saved = productRepository.save(existingProduct);
        return mapper.toResponseDTO(saved);
    }

    public ProductResponseDTO updateProductByName(String name, ProductRequestDTO desiredDTO) {
        Product existingProduct = productRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + name));

        updateProduct(existingProduct, desiredDTO);

        Product saved = productRepository.save(existingProduct);
        return mapper.toResponseDTO(saved);
    }

    public ProductResponseDTO updateProductByCode(String code, ProductRequestDTO desiredDTO) {
        Product existingProduct = productRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + code));

        updateProduct(existingProduct, desiredDTO);

        Product saved = productRepository.save(existingProduct);
        return mapper.toResponseDTO(saved);
    }

    //DELETE
    public void deleteProductById(Long id) {
        if(!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found: " + id);
        }
        productRepository.deleteById(id);
    }

    public void deleteProductByName(String name) {
        if(!productRepository.existsByName(name)) {
            throw new IllegalArgumentException("Product not found: " + name);
        }
        productRepository.deleteByName(name);
    }

    public void deleteProductByCode(String code) {
        if(!productRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Product not found: " + code);
        }
        productRepository.deleteByCode(code);
    }
}


