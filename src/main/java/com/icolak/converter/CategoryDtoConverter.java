package com.icolak.converter;

import com.icolak.dto.CategoryDTO;
import com.icolak.service.CategoryService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryDtoConverter implements Converter<String, CategoryDTO> {

    private final CategoryService categoryService;

    public CategoryDtoConverter(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public CategoryDTO convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return categoryService.findById(Long.parseLong(source));
    }
}

