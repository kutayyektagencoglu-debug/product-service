package com.migros.productservice.client;

import com.migros.productservice.dto.BarcodeRequestDTO;
import com.migros.productservice.dto.BarcodeResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "barcode-service", url = "http://localhost:8082")
public interface BarcodeClient {
    @PostMapping("/api/v1/barcode")
    List<BarcodeResponseDTO> createBarcode(BarcodeRequestDTO barcodeRequestDTO);
    @DeleteMapping("/api/v1/barcode/productCode/{productCode}")
    void deleteBarcode(@PathVariable String productCode);
}