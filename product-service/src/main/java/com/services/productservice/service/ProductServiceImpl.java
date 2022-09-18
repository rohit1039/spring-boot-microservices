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

@Service
@Log4j2
public class ProductServiceImpl implements ProductService
{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO)
    {
        log.info("Inside addProduct method");
        Product product = this.modelMapper.map(productDTO, Product.class);
        ProductDTO dto = this.modelMapper.map(this.productRepository.save(product), ProductDTO.class);
        log.info("Product added successfully");

        return dto;
    }

    @Override
    public List<ProductDTO> getProducts()
    {
        log.info("Inside getProducts method");
        List<Product> list = this.productRepository.findAll();
        List<ProductDTO> allProducts = list.stream().map(product -> this.modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

        return allProducts;
    }

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

    @Override
    public ProductDTO reduceQuantity(Long productId, Long quantity)
    {
        Product product = this.productRepository.findById(productId).orElseThrow(() ->
            (
                new ProductServiceCustomException("Product not found with ID: " + productId, "PRODUCT_NOT_FOUND")
            ));

        if (product.getQuantity() < quantity || quantity < 0)
        {
            throw new ProductServiceCustomException("Quantity must be equals to or less than original quantity!", "INSUFFICIENT_QUANTITY");
        }

        long updatedQuantity = product.getQuantity() - quantity;
        product.setQuantity(updatedQuantity);
        ProductDTO productDTO = this.modelMapper.map(this.productRepository.save(product), ProductDTO.class);
        log.info("Product quantity updated successfully!");

        return productDTO;
    }
}
