package com.icolak.controller;

import com.icolak.dto.ProductDTO;
import com.icolak.enums.ProductUnit;
import com.icolak.service.CategoryService;
import com.icolak.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

        model.addAttribute("products", productService.listAllProducts());

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
    public String insertProduct(@Valid @ModelAttribute("newProduct") ProductDTO productDTO) {
        productService.save(productDTO);
        return "redirect:/products/list";
    }
}
