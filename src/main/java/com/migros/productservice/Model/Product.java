package com.migros.productservice.Model;

import com.migros.productservice.enums.UnitType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, length = 5, nullable = false)
    private String code;

    @Column(length = 3, nullable = false)
    private int categoryNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private String categoryCode;

    @Column(nullable = false)
    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitType unit;

}
