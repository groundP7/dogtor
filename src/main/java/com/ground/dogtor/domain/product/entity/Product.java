package com.ground.dogtor.domain.product.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private Long adminId;
    private String name;
    private String category;
    private String description;
    private int price;
    private int stock;
    private String imageUrl;
    private String createdAt;
    
}