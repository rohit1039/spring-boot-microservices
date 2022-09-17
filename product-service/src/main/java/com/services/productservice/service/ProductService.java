package com.services.productservice.service;

import com.services.productservice.model.ProductDTO;

import java.util.List;

/**
 * @author - ROHIT PARIDA
 */
public interface ProductService
{
    /**
     *
     * @param productDTO
     * @return
     */
    public ProductDTO addProduct(ProductDTO productDTO);

    /**
     *
     * @return
     */
    public List<ProductDTO> getProducts();

    /**
     *
     * @param productId
     * @return
     */
    public ProductDTO getProductById(Long productId);

    public ProductDTO reduceQuantity(Long productId, Long quantity);
}
