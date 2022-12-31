package com.icolak.controller;

import com.icolak.bootstrap.StaticConstants;
import com.icolak.dto.CompanyDTO;
import com.icolak.enums.CompanyStatus;
import com.icolak.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public String insertCompany(@Valid @ModelAttribute("newCompany") CompanyDTO company,
                                BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("countries", StaticConstants.COUNTRY_LIST);
            return "/company/company-create";
        }

        if (companyService.isTitleExist(company.getTitle())) {
            bindingResult.rejectValue("title", " ", "This title already exists");
        }

        companyService.save(company);

        return "redirect:/companies/list";
    }

    @GetMapping("/activate/{id}")
    public String activateCompany(@PathVariable("id") Long id) {

        companyService.activateCompanyStatus(id);

        return "redirect:/companies/list";
    }

    @GetMapping("/deactivate/{id}")
    public String deactivateCompany(@PathVariable("id") Long id) {

        companyService.deactivateCompanyStatus(id);

        return "redirect:/companies/list";
    }

    @GetMapping("/update/{id}")
    public String editCompany(@PathVariable("id") Long id, Model model) {

        model.addAttribute("company", companyService.findById(id));
        model.addAttribute("countries", StaticConstants.COUNTRY_LIST);

        return "/company/company-update";
    }

    @PostMapping("/update/{id}")
    public String updateCompany(@Valid @ModelAttribute("company") CompanyDTO companyDTO,
                                BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("countries", StaticConstants.COUNTRY_LIST);
            return "/company/company-update";
        }

        companyService.update(companyDTO);

        return "redirect:/companies/list";
    }
}
