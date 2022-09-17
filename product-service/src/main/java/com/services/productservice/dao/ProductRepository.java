package com.services.productservice.dao;

import com.services.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author - ROHIT PARIDA
 */
public interface ProductRepository extends JpaRepository<Product,Long>
{
}
