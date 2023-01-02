package com.icolak.service;

import com.icolak.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO findById(Long id);

    List<CategoryDTO> listAllCategories();
}
