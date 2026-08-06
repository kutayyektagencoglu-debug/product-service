package com.migros.productservice.repository;

import com.migros.productservice.Model.Product;
import com.migros.productservice.enums.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsById(Long id);
    boolean existsByCode(String code);
    boolean existsByName(String name);

    Optional<Product> findByCode(String code);
    Optional<Product> findByName(String Name);

    List<Product> findByUnit(UnitType Unit);
    List<Product> findByBrand(String brand);
    List<Product> findByCategoryCode(String categoryCode);

    void deleteById(Long id);
    void deleteByName(String name);
    void deleteByCode(String code);

}
