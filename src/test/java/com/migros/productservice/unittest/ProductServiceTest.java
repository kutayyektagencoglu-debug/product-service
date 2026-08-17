package com.migros.productservice.unittest;

import com.migros.commonerror.exception.BusinessException;
import com.migros.productservice.Model.Product;
import com.migros.productservice.client.BarcodeClient;
import com.migros.productservice.client.CategoryClient;
import com.migros.productservice.dto.ProductRequestDTO;
import com.migros.productservice.dto.ProductResponseDTO;
import com.migros.productservice.enums.UnitType;
import com.migros.productservice.mapper.ProductMapper;
import com.migros.productservice.repository.ProductRepository;
import com.migros.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Unit Tests")
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper mapper;
    @Mock
    private CategoryClient categoryClient;
    @Mock
    private BarcodeClient barcodeClient;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    //Create test
    @Test
    void createProductThrowsWhenNameExists() {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName("elma");

        Product product = new Product();
        product.setName("elma");

        when(mapper.toEntity(dto)).thenReturn(product);
        when(productRepository.existsByName("elma")).thenReturn(true);

        assertThrows(BusinessException.class, () -> productService.createProduct(dto));
    }
    @Test
    void createProductThrowsWhenWrongCategoryCode() {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName("elma");
        dto.setCategoryCode("MY");

        Product product = new Product();
        product.setName("elma");
        product.setCategoryCode("MY");

        when(mapper.toEntity(dto)).thenReturn(product);
        when(productRepository.existsByName("elma")).thenReturn(false);

        when(categoryClient.verifyCategoryCode("MY")).thenReturn(false);

        assertThrows(BusinessException.class, () -> productService.createProduct(dto));
    }

    @Test
    void createProductThrowsWhenHighCategoryNumber() {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName("elma");
        dto.setCategoryCode("MY");

        Product product = new Product();
        product.setName("elma");
        product.setCategoryCode("MY");

        when(mapper.toEntity(dto)).thenReturn(product);
        when(productRepository.existsByName("elma")).thenReturn(false);

        when(categoryClient.verifyCategoryCode("MY")).thenReturn(true);
        when(productRepository.findMaxCategoryNumber("MY")).thenReturn(999);

        assertThrows(BusinessException.class, () -> productService.createProduct(dto));
    }

    @Test
    void createProductSuccess() {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName("elma");
        dto.setCategoryCode("MY");
        dto.setUnit(UnitType.KILOGRAM);

        Product product = new Product();
        product.setName("elma");
        product.setCategoryCode("MY");
        product.setUnit(UnitType.KILOGRAM);

        when(mapper.toEntity(dto)).thenReturn(product);
        when(productRepository.existsByName("elma")).thenReturn(false);

        when(categoryClient.verifyCategoryCode("MY")).thenReturn(true);
        when(productRepository.findMaxCategoryNumber("MY")).thenReturn(0);

        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(mapper.toResponseDTO(product)).thenReturn(new ProductResponseDTO());

        ProductResponseDTO response = productService.createProduct(dto);

        assertNotNull(response);
        assertEquals(1, product.getCategoryNumber());
        assertEquals("MY001", product.getCode());
        verify(barcodeClient).createBarcode(any());
        verify(productRepository).save(product);
    }

    //Update tests
    @Test
    void updateProductByCodeThrowsWhenCodeNotFound() {
        String code = "MY001";

        ProductRequestDTO dto = new ProductRequestDTO();

        when(productRepository.findByCode("MY001")).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> productService.updateProductByCode(code, dto));
    }

    @Test
    void updateProductByCodeChangeNameBrandSuccess() {
        String code = "MY001";

        ProductRequestDTO desiredDTO = new ProductRequestDTO();
        desiredDTO.setName("armut");
        desiredDTO.setBrand("bca");
        desiredDTO.setCategoryCode("MY");
        desiredDTO.setUnit(UnitType.KILOGRAM);

        Product existingProduct = new Product();
        existingProduct.setName("elma");
        existingProduct.setBrand("abc");
        existingProduct.setCategoryCode("MY");
        existingProduct.setUnit(UnitType.KILOGRAM);

        when(productRepository.findByCode("MY001")).thenReturn(Optional.of(existingProduct));

        when(productRepository.existsByName("armut")).thenReturn(false);

        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);
        when(mapper.toResponseDTO(existingProduct)).thenReturn(new ProductResponseDTO());

        ProductResponseDTO response = productService.updateProductByCode(code, desiredDTO);

        assertNotNull(response);
        assertEquals("armut", existingProduct.getName());
        assertEquals("bca", existingProduct.getBrand());
        verify(barcodeClient, never()).deleteBarcode(any());
        verify(barcodeClient, never()).createBarcode(any());
        verify(productRepository).save(existingProduct);
    }

    @Test
    void updateProductByCodeChangeCategoryCodeInvalid() {
        String code = "MY001";

        ProductRequestDTO desiredDTO = new ProductRequestDTO();
        desiredDTO.setName("elma");
        desiredDTO.setBrand("abc");
        desiredDTO.setCategoryCode("ME");
        desiredDTO.setUnit(UnitType.KILOGRAM);

        Product existingProduct = new Product();
        existingProduct.setName("elma");
        existingProduct.setBrand("abc");
        existingProduct.setCategoryCode("MY");
        existingProduct.setUnit(UnitType.KILOGRAM);

        when(productRepository.findByCode("MY001")).thenReturn(Optional.of(existingProduct));

        when(categoryClient.verifyCategoryCode("ME")).thenReturn(false);

        assertThrows(BusinessException.class, () -> productService.updateProductByCode(code, desiredDTO));
    }

    @Test
    void updateProductByCodeChangeCategoryCodeSuccess() {
        String code = "MY001";

        ProductRequestDTO desiredDTO = new ProductRequestDTO();
        desiredDTO.setName("elma");
        desiredDTO.setBrand("abc");
        desiredDTO.setCategoryCode("ME");
        desiredDTO.setUnit(UnitType.KILOGRAM);

        Product existingProduct = new Product();
        existingProduct.setName("elma");
        existingProduct.setBrand("abc");
        existingProduct.setCategoryCode("MY");
        existingProduct.setUnit(UnitType.KILOGRAM);

        when(productRepository.findByCode("MY001")).thenReturn(Optional.of(existingProduct));

        when(categoryClient.verifyCategoryCode("ME")).thenReturn(true);
        when(productRepository.findMaxCategoryNumber("ME")).thenReturn(0);

        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);
        when(mapper.toResponseDTO(existingProduct)).thenReturn(new ProductResponseDTO());

        ProductResponseDTO response = productService.updateProductByCode(code, desiredDTO);

        assertNotNull(response);
        assertEquals("ME", existingProduct.getCategoryCode());
        assertEquals("ME001", existingProduct.getCode());
        verify(barcodeClient).deleteBarcode(any());
        verify(barcodeClient).createBarcode(any());
        verify(productRepository).save(existingProduct);
    }

    @Test
    void updateProductByCodeChangeUnitSuccess() {
        String code = "MY001";

        ProductRequestDTO desiredDTO = new ProductRequestDTO();
        desiredDTO.setName("elma");
        desiredDTO.setBrand("abc");
        desiredDTO.setCategoryCode("MY");
        desiredDTO.setUnit(UnitType.NUMBER);

        Product existingProduct = new Product();
        existingProduct.setName("elma");
        existingProduct.setBrand("abc");
        existingProduct.setCategoryCode("MY");
        existingProduct.setUnit(UnitType.KILOGRAM);

        when(productRepository.findByCode("MY001")).thenReturn(Optional.of(existingProduct));

        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);
        when(mapper.toResponseDTO(existingProduct)).thenReturn(new ProductResponseDTO());

        ProductResponseDTO response = productService.updateProductByCode(code, desiredDTO);

        assertNotNull(response);
        assertEquals(UnitType.NUMBER, existingProduct.getUnit());
        verify(barcodeClient).deleteBarcode(any());
        verify(barcodeClient).createBarcode(any());
        verify(productRepository).save(existingProduct);
    }
    //Delete tests
    @Test
    void deleteProductByCodeThrowsWhenNotFound() {
        when(productRepository.existsByCode("MY001")).thenReturn(false);

        assertThrows(BusinessException.class,
                () -> productService.deleteProductByCode("MY001"));
    }

    @Test
    void deleteProductByCodeSuccess() {
        when(productRepository.existsByCode("MY001")).thenReturn(true);

        productService.deleteProductByCode("MY001");

        verify(barcodeClient).deleteBarcode("MY001");
        verify(productRepository).deleteByCode("MY001");
    }

}
