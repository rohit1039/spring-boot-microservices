package com.services.productservice.controller;

import com.services.productservice.model.ProductDTO;
import com.services.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/product")
public class ProductController
{
    @Autowired
    private ProductService productService;

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping("/add")
    public ResponseEntity<EntityModel<ProductDTO>> addProduct(@Valid @RequestBody ProductDTO productDTO)
    {
        EntityModel<ProductDTO> entityModel = EntityModel.of(this.productService.addProduct(productDTO));
        WebMvcLinkBuilder linkBuilder = linkTo(methodOn(this.getClass()).getAllProducts());
        entityModel.add(linkBuilder.withRel("get-all-products"));

        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer') || hasAuthority('SCOPE_internal')")
    @GetMapping("/get-all")
    public ResponseEntity<List<ProductDTO>> getAllProducts()
    {
        List<ProductDTO> products = this.productService.getProducts();

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer') || hasAuthority('SCOPE_internal')")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId)
    {
        ProductDTO productDTO = this.productService.getProductById(productId);

        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer') || hasAuthority('SCOPE_internal')")
    @PutMapping("/reduce-quantity/{productId}")
    public ResponseEntity<EntityModel<ProductDTO>> reduceQuantity(@PathVariable Long productId, @RequestParam Long quantity)
    {
        EntityModel<ProductDTO> entityModel = EntityModel.of(this.productService.reduceQuantity(productId,quantity));
        WebMvcLinkBuilder linkBuilder = linkTo(methodOn(this.getClass()).getAllProducts());
        entityModel.add(linkBuilder.withRel("get-all-products"));

        return new ResponseEntity<>(entityModel, HttpStatus.OK);
    }
}



