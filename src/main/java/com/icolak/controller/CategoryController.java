package com.icolak.controller;

import com.icolak.dto.CategoryDTO;
import com.icolak.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String getCategories(Model model) {
        model.addAttribute("categories", categoryService.listAllCategories());
        return "/category/category-list";
    }

    @GetMapping("/create")
    public String createCategory(Model model) {
        model.addAttribute("newCategory", new CategoryDTO());
        return "/category/category-create";
    }

    @PostMapping("/create")
    public String insertCategory(@Valid @ModelAttribute("newCategory") CategoryDTO categoryDTO,
                                 BindingResult bindingResult) {
        if (categoryService.isDescriptionExist(categoryDTO.getDescription())) {
            bindingResult.rejectValue("description", "", "This category already exists");
        }
        if (bindingResult.hasErrors()) {
            return "/category/category-create";
        }
        categoryService.save(categoryDTO);
        return "redirect:/categories/list";
    }
}
