package com.services.productservice.controller;

import com.services.productservice.model.ProductDTO;
import com.services.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author - ROHIT PARIDA
 */
@RestController
@RequestMapping("/product")
public class ProductController
{
    @Autowired
    private ProductService productService;

    /**
     *
     * @param productDTO
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<EntityModel<ProductDTO>> addProduct(@Valid @RequestBody ProductDTO productDTO)
    {
        EntityModel<ProductDTO> entityModel = EntityModel.of(this.productService.addProduct(productDTO));
        WebMvcLinkBuilder linkBuilder = linkTo(methodOn(this.getClass()).getAllProducts());
        entityModel.add(linkBuilder.withRel("get-all-products"));

        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    /**
     *
     * @return
     */
    @GetMapping("/get-all")
    public ResponseEntity<List<ProductDTO>> getAllProducts()
    {
        List<ProductDTO> products = this.productService.getProducts();

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     *
     * @param productId
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable(name = "id") Long productId)
    {
        ProductDTO productDTO = this.productService.getProductById(productId);

        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }
}



