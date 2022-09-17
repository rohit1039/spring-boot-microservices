package com.services.productservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author - ROHIT PARIDA
 */
@SpringBootApplication
public class ProductServiceApplication {

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

	/**
	 *
	 * @return
	 */
	@Bean
	public ModelMapper modelMapper()
	{
		return new ModelMapper();
	}

}
