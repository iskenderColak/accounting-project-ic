package com.icolak.service.implementation;

import com.icolak.dto.CategoryDTO;
import com.icolak.dto.CompanyDTO;
import com.icolak.entity.Category;
import com.icolak.entity.Company;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.CategoryRepository;
import com.icolak.service.CategoryService;
import com.icolak.service.ProductService;
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
    private final ProductService productService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, MapperUtil mapperUtil, SecurityService securityService, ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
        this.productService = productService;
    }

    @Override
    public CategoryDTO findById(Long id) {
        CategoryDTO categoryDTO = mapperUtil.convert(categoryRepository.findById(id).orElseThrow(), new CategoryDTO());
        categoryDTO.setHasProduct(productService.isExistByCategoryId(id));
        return categoryDTO;
    }

    @Override
    public List<CategoryDTO> listAllCategories() {
        return categoryRepository.findAll(Sort.by("description")).stream()
                .filter(category -> category.getCompany().getId().equals(securityService.getLoggedInUser().getCompany().getId()))
                .map(category -> {
                    CategoryDTO dto = mapperUtil.convert(category, new CategoryDTO());
                    dto.setHasProduct(productService.isExistByCategoryId(dto.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category = mapperUtil.convert(categoryDTO, new Category());
        category.setCompany(mapperUtil.convert(securityService.getLoggedInUser().getCompany(), new Company()));
        categoryRepository.save(category);
    }

    @Override
    public boolean isDescriptionExist(String description, CompanyDTO companyDTO) {
        return categoryRepository.existsByDescriptionAndCompany
                (description, mapperUtil.convert(companyDTO, new Company()));
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryDTO) {
        save(categoryDTO);
        return findById(categoryDTO.getId());
    }

    @Override
    public boolean isDescriptionExistExceptCurrentDescription(CategoryDTO categoryDTO) {
        Category category = mapperUtil.convert(findById(categoryDTO.getId()), new Category());
        if (category.getDescription().equals(categoryDTO.getDescription())) {
            return false;
        }
        return isDescriptionExist(categoryDTO.getDescription(),
                mapperUtil.convert(category.getCompany(), new CompanyDTO()));
    }

    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow();
        category.setIsDeleted(true);
        categoryRepository.save(category);
    }
}
