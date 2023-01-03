package com.icolak.converter;

import com.icolak.dto.ProductDTO;
import com.icolak.service.ProductService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductDtoConverter implements Converter<String, ProductDTO> {

    private final ProductService productService;

    public ProductDtoConverter(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ProductDTO convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return productService.findById(Long.parseLong(source));
    }
}
