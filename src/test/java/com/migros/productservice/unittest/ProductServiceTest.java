package com.migros.productservice.unittest;

import com.migros.productservice.Model.Product;
import com.migros.productservice.client.BarcodeClient;
import com.migros.productservice.client.CategoryClient;
import com.migros.productservice.enums.UnitType;
import com.migros.productservice.mapper.ProductMapper;
import com.migros.productservice.repository.ProductRepository;
import com.migros.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Unit Tests")
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private CategoryClient categoryClient;
    @Mock
    private BarcodeClient barcodeClient;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setBrand("Test Brand");
        testProduct.setUnit(UnitType.KILOGRAM);
        testProduct.setCategoryCode("MY");
    }
}
