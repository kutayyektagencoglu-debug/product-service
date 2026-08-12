package com.migros.productservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "category-service", url = "http://localhost:8081")
public interface CategoryClient {
    @GetMapping("/api/v1/category/verify/{code}")
    boolean verifyCategoryCode(@PathVariable String code);
}
