package com.services.productservice.service;

import com.services.productservice.model.ProductDTO;

import java.util.List;

public interface ProductService
{
    public ProductDTO addProduct(ProductDTO productDTO);

    public List<ProductDTO> getProducts();

    public ProductDTO getProductById(Long productId);

    public ProductDTO reduceQuantity(Long productId, Long quantity);
}
