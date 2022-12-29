package com.icolak.controller;

import com.icolak.bootstrap.StaticConstants;
import com.icolak.dto.CompanyDTO;
import com.icolak.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/list")
    public String getCompanies(Model model) {

        model.addAttribute("companies", companyService.listAllCompanies());

        return "company/company-list";
    }

    @GetMapping("/create")
    public String createCompany(Model model) {

        model.addAttribute("newCompany", new CompanyDTO());
        model.addAttribute("countries", StaticConstants.COUNTRY_LIST);

        return "company/company-create";
    }

    @PostMapping("/create")
    public String insertCompany(@ModelAttribute CompanyDTO company) {

        companyService.save(company);

        return "redirect:/companies/create";
    }
}
