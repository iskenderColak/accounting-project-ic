package com.icolak.controller;

import com.icolak.dto.ProductDTO;
import com.icolak.enums.ProductUnit;
import com.icolak.service.CategoryService;
import com.icolak.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String getProducts(Model model) {
        model.addAttribute("products", productService.listAllProductsByCompany());
        return "/product/product-list";
    }

    @GetMapping("/create")
    public String createProduct(Model model) {
        model.addAttribute("newProduct", new ProductDTO());
        model.addAttribute("categories", categoryService.listAllCategories());
        model.addAttribute("productUnits", ProductUnit.values());
        return "/product/product-create";
    }

    @PostMapping("/create")
    public String insertProduct(@Valid @ModelAttribute("newProduct") ProductDTO productDTO,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.listAllCategories());
            model.addAttribute("productUnits", ProductUnit.values());
            return "/product/product-create";
        }
        productService.save(productDTO);
        return "redirect:/products/list";
    }

    @GetMapping("/update/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("categories", categoryService.listAllCategories());
        model.addAttribute("productUnits", ProductUnit.values());
        return "/product/product-update";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@Valid @ModelAttribute("product") ProductDTO productDTO,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.listAllCategories());
            model.addAttribute("productUnits", ProductUnit.values());
            return "/product/product-update";
        }
        productService.update(productDTO);
        return "redirect:/products/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.delete(id);
        return "redirect:/products/list";
    }
}
