package com.services.productservice.service;

import com.services.productservice.dao.ProductRepository;
import com.services.productservice.entity.Product;
import com.services.productservice.exception.ProductServiceCustomException;
import com.services.productservice.model.ProductDTO;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author - ROHIT PARIDA
 */
@Service
@Log4j2
public class ProductServiceImpl implements ProductService
{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     *
     * @param productDTO
     * @return
     */
    @Override
    public ProductDTO addProduct(ProductDTO productDTO)
    {
        log.info("Inside addProduct method");
        Product product = this.modelMapper.map(productDTO, Product.class);
        ProductDTO dto = this.modelMapper.map(this.productRepository.save(product), ProductDTO.class);
        log.info("Product added successfully");

        return dto;
    }

    /**
     *
     * @return
     */
    @Override
    public List<ProductDTO> getProducts()
    {
        log.info("Inside getProducts method");
        List<Product> list = this.productRepository.findAll();
        List<ProductDTO> allProducts = list.stream().map(product -> this.modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

        return allProducts;
    }

    /**
     *
     * @param productId
     * @return
     */
    @Override
    public ProductDTO getProductById(Long productId)
    {
        log.info("Inside getProductById method");
        Product product = this.productRepository.findById(productId).orElseThrow(() ->
            (
                new ProductServiceCustomException("No product found with ID: " + productId, "PRODUCT_NOT_FOUND")
            ));
        return this.modelMapper.map(product, ProductDTO.class);
    }
}
