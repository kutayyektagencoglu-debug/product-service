package com.migros.productservice.service;

import com.migros.productservice.Model.Product;
import com.migros.productservice.client.BarcodeClient;
import com.migros.productservice.client.CategoryClient;
import com.migros.productservice.dto.BarcodeRequestDTO;
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
    private final CategoryClient categoryClient;
    private final BarcodeClient barcodeClient;

    public ProductService(ProductRepository productRepository, ProductMapper mapper,
                          CategoryClient categoryClient, BarcodeClient barcodeClient) {
        this.mapper = mapper;
        this.productRepository = productRepository;
        this.categoryClient = categoryClient;
        this.barcodeClient = barcodeClient;
    }

    public void assignCode(Product product) {
        if(!categoryClient.verifyCategoryCode(product.getCategoryCode())) {
            throw new IllegalArgumentException("Invalid category code (Category code has to be 2 letters long)");
        }
        String categoryCode = product.getCategoryCode();
        //ASSIGN CATEGORY CODE
        int maxCategoryNumber = productRepository.findMaxCategoryNumber(categoryCode);
        int nextCategoryNumber = maxCategoryNumber + 1;
        product.setCategoryNumber(nextCategoryNumber);
        //ASSIGN CODE
        String code = categoryCode + nextCategoryNumber;
        product.setCode(code);
    }

    public void generateBarcode(Product product) {
        BarcodeRequestDTO barcodeRequestDTO = new BarcodeRequestDTO(product.getCode());
        barcodeClient.createBarcode(barcodeRequestDTO);
    }
    //CREATE
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Product product = mapper.toEntity(dto);
        if(productRepository.existsByName(product.getName())) {
            throw new IllegalArgumentException("Product name already exists");
        }
        assignCode(product);
        generateBarcode(product);

        Product saved = productRepository.save(product);
        return mapper.toResponseDTO(saved);
    }

    //READ ALL
    public List<ProductResponseDTO> getAllProducts(){
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
        existingProduct.setBrand(desiredDTO.getBrand());

        UnitType unit = existingProduct.getUnit();
        existingProduct.setUnit(desiredDTO.getUnit());

        String categoryCode = desiredDTO.getCategoryCode();
        if(!categoryCode.equals(existingProduct.getCategoryCode())) {
            existingProduct.setCategoryCode(categoryCode);
            assignCode(existingProduct);

            barcodeClient.deleteBarcode(existingProduct.getCode());
            generateBarcode(existingProduct);
        } else if(!(unit == desiredDTO.getUnit())) {
            barcodeClient.deleteBarcode(existingProduct.getCode());
            generateBarcode(existingProduct);
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
    public void deleteProductByCode(String code) {
        if(!productRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Product not found: " + code);
        }
        barcodeClient.deleteBarcode(code);
        productRepository.deleteByCode(code);
    }
}


