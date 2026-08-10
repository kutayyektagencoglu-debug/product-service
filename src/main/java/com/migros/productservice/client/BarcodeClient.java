package com.migros.productservice.client;

import com.migros.productservice.dto.BarcodeRequestDTO;
import com.migros.productservice.dto.BarcodeResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(name = "barcode-service", url = "http://localhost:8082")
public interface BarcodeClient {
    List<BarcodeResponseDTO> createBarcode(BarcodeRequestDTO barcodeRequestDTO);
}