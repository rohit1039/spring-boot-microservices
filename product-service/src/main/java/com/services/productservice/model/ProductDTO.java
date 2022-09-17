package com.services.productservice.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
public class ProductDTO
{
    private Long productId;
    @NotEmpty(message = "Product name cannot be empty!")
    private String productName;
    @PositiveOrZero
    private Double price;
    @Positive
    private Long quantity;

}
