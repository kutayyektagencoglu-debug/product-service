package com.migros.productservice.integrationtest;

import com.migros.commonerror.exception.BusinessException;
import com.migros.productservice.Model.Product;
import com.migros.productservice.client.BarcodeClient;
import com.migros.productservice.client.CategoryClient;
import com.migros.productservice.dto.BarcodeResponseDTO;
import com.migros.productservice.dto.ProductRequestDTO;
import com.migros.productservice.dto.ProductResponseDTO;
import com.migros.productservice.enums.UnitType;
import com.migros.productservice.repository.ProductRepository;
import com.migros.productservice.service.ProductService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProductServiceIntegrationTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @MockBean
    private CategoryClient categoryClient;
    @MockBean
    private BarcodeClient barcodeClient;

    @Test
    public void createProductThrowsNameAlreadyExists() {
        ProductRequestDTO dto1 = new ProductRequestDTO();
        dto1.setName("test");
        dto1.setBrand("tesco");
        dto1.setUnit(UnitType.KILOGRAM);
        dto1.setCategoryCode("TS");

        ProductRequestDTO dto2 = new ProductRequestDTO();
        dto2.setName("test");
        dto2.setBrand("asd");
        dto2.setUnit(UnitType.NUMBER);
        dto2.setCategoryCode("AS");

        when(categoryClient.verifyCategoryCode(anyString())).thenReturn(true);
        when(barcodeClient.createBarcode(any())).thenReturn(List.of(new BarcodeResponseDTO()));

        productService.createProduct(dto1);

        assertThrows(BusinessException.class, () -> productService.createProduct(dto2));

        assertTrue(productRepository.findByBrand("asd").isEmpty());
    }
    @Test
    public void createProductSuccess() {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName("test");
        dto.setBrand("tesco");
        dto.setUnit(UnitType.KILOGRAM);
        dto.setCategoryCode("TS");

        when(categoryClient.verifyCategoryCode("TS")).thenReturn(true);
        when(barcodeClient.createBarcode(any())).thenReturn(List.of(new BarcodeResponseDTO()));

        ProductResponseDTO result = productService.createProduct(dto);

        verify(barcodeClient, times(1)).createBarcode(any());
        verify(categoryClient, times(1)).verifyCategoryCode("TS");

        assertEquals("test", result.getName());
        assertEquals("tesco", result.getBrand());
        assertEquals(UnitType.KILOGRAM, result.getUnit());
        assertEquals("TS", result.getCategoryCode());
        assertEquals("TS001", result.getCode());

        Product saved =  productRepository.findByCode(result.getCode()).orElseThrow();
        assertEquals("test", saved.getName());
        assertEquals("tesco", saved.getBrand());
        assertEquals(UnitType.KILOGRAM, saved.getUnit());
        assertEquals("TS001", saved.getCode());
    }

    @Test
    public void getProductByCodeSuccess() {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName("test");
        dto.setBrand("tesco");
        dto.setUnit(UnitType.KILOGRAM);
        dto.setCategoryCode("TS");

        when(categoryClient.verifyCategoryCode("TS")).thenReturn(true);
        when(barcodeClient.createBarcode(any())).thenReturn(List.of(new BarcodeResponseDTO()));
        productService.createProduct(dto);

        ProductResponseDTO result = productService.getProductByCode("TS001");
        assertEquals("test", result.getName());
    }

    @Test
    public void getProductByBrandSuccess() {
        ProductRequestDTO dto1 = new ProductRequestDTO();
        dto1.setName("test");
        dto1.setBrand("tesco");
        dto1.setUnit(UnitType.KILOGRAM);
        dto1.setCategoryCode("TS");

        ProductRequestDTO dto2 = new ProductRequestDTO();
        dto2.setName("tset");
        dto2.setBrand("asd");
        dto2.setUnit(UnitType.NUMBER);
        dto2.setCategoryCode("BL");

        ProductRequestDTO dto3 = new ProductRequestDTO();
        dto3.setName("odd");
        dto3.setBrand("tesco");
        dto3.setUnit(UnitType.NUMBER);
        dto3.setCategoryCode("MY");

        when(categoryClient.verifyCategoryCode(any())).thenReturn(true);
        when(barcodeClient.createBarcode(any())).thenReturn(List.of(new BarcodeResponseDTO()));
        productService.createProduct(dto1);
        productService.createProduct(dto2);
        productService.createProduct(dto3);

        List<ProductResponseDTO> result = productService.getProductByBrand("tesco");
        assertEquals(2,  result.size());
        assertTrue(result.stream().allMatch(b -> "tesco".equals(b.getBrand())));
    }

    @Test
    public void updateProductByCodeSuccess() {
        ProductRequestDTO oldDTO = new ProductRequestDTO();
        oldDTO.setName("oldtest");
        oldDTO.setBrand("tesco");
        oldDTO.setUnit(UnitType.KILOGRAM);
        oldDTO.setCategoryCode("TS");

        ProductRequestDTO newDTO = new ProductRequestDTO();
        newDTO.setName("newtest");
        newDTO.setBrand("walmart");
        newDTO.setUnit(UnitType.NUMBER);
        newDTO.setCategoryCode("NT");

        when(categoryClient.verifyCategoryCode(any())).thenReturn(true);
        when(barcodeClient.createBarcode(any())).thenReturn(List.of(new BarcodeResponseDTO()));
        doNothing().when(barcodeClient).deleteBarcode(any());

        productService.createProduct(oldDTO);
        ProductResponseDTO result = productService.updateProductByCode("TS001", newDTO);

        verify(barcodeClient, times(2)).createBarcode(any());
        verify(categoryClient, times(1)).verifyCategoryCode("NT");
        verify(barcodeClient, times(1)).deleteBarcode("TS001");

        assertEquals("newtest", result.getName());
        assertEquals("walmart", result.getBrand());
        assertEquals(UnitType.NUMBER, result.getUnit());
        assertEquals("NT001", result.getCode());
    }

    @Test
    public void deleteProductByCodeSuccess() {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName("test");
        dto.setBrand("tesco");
        dto.setUnit(UnitType.KILOGRAM);
        dto.setCategoryCode("TS");
        when(categoryClient.verifyCategoryCode("TS")).thenReturn(true);
        when(barcodeClient.createBarcode(any())).thenReturn(List.of(new BarcodeResponseDTO()));
        doNothing().when(barcodeClient).deleteBarcode(any());
        
        productService.createProduct(dto);

        ProductResponseDTO result = productService.getProductByCode("TS001");
        assertNotNull(result);

        productService.deleteProductByCode("TS001");
        assertThrows(BusinessException.class, () -> productService.getProductByCode("TS001"));
        verify(barcodeClient, times(1)).deleteBarcode("TS001");
    }
}
