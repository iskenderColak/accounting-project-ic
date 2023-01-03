package com.icolak.service;

import com.icolak.dto.CategoryDTO;
import com.icolak.dto.CompanyDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO findById(Long id);

    List<CategoryDTO> listAllCategories();

    void save(CategoryDTO categoryDTO);

    boolean isDescriptionExist(String description, CompanyDTO companyDTO);

    CategoryDTO update(CategoryDTO categoryDTO);

    boolean isDescriptionExistExceptCurrentDescription(CategoryDTO categoryDTO);
}
