package com.icolak.controller;

import com.icolak.dto.CategoryDTO;
import com.icolak.service.CategoryService;
import com.icolak.service.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final SecurityService securityService;

    public CategoryController(CategoryService categoryService, SecurityService securityService) {
        this.categoryService = categoryService;
        this.securityService = securityService;
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
        if (categoryService.isDescriptionExist(categoryDTO.getDescription(),
                 securityService.getLoggedInUser().getCompany())) {
            bindingResult.rejectValue("description", "", "This category already exists in this company");
        }
        if (bindingResult.hasErrors()) {
            return "/category/category-create";
        }
        categoryService.save(categoryDTO);
        return "redirect:/categories/list";
    }

    @GetMapping("/update/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model) {
        model.addAttribute("category", categoryService.findById(id));
        return "/category/category-update";
    }

    @PostMapping("/update/{id}")
    public String updateCategory(@Valid @ModelAttribute("category") CategoryDTO categoryDTO,
                                 BindingResult bindingResult) {
        if (categoryService.isDescriptionExistExceptCurrentDescription(categoryDTO)) {
            bindingResult.rejectValue("description", "", "This category already exists in this company");
        }
        if (bindingResult.hasErrors()) {
            return "/category/category-update";
        }
        categoryService.update(categoryDTO);
        return "redirect:/categories/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return "redirect:/categories/list";
    }
}
