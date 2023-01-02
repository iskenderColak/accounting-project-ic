package com.icolak.service.implementation;

import com.icolak.dto.CategoryDTO;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.CategoryRepository;
import com.icolak.service.CategoryService;
import com.icolak.service.SecurityService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MapperUtil mapperUtil;

    private final SecurityService securityService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, MapperUtil mapperUtil, SecurityService securityService) {
        this.categoryRepository = categoryRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
    }

    @Override
    public CategoryDTO findById(Long id) {
        return mapperUtil.convert(categoryRepository.findById(id).orElseThrow(), new CategoryDTO());
    }

    @Override
    public List<CategoryDTO> listAllCategories() {
        return categoryRepository.findAll(Sort.by("description")).stream()
                .filter(category -> category.getCompany().getTitle().equals(securityService.getLoggedInUser().getCompany().getTitle()))
                .map(category -> mapperUtil.convert(category, new CategoryDTO()))
                .collect(Collectors.toList());
    }
}
