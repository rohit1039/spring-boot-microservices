package com.services.orderservice.proxy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO
{
    private Long productId;
    private String productName;
    @JsonIgnore
    private Double price;
    @JsonIgnore
    private Long quantity;
}
