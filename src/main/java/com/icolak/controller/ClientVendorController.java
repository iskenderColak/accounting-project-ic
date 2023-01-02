package com.icolak.controller;

import com.icolak.bootstrap.StaticConstants;
import com.icolak.dto.ClientVendorDTO;
import com.icolak.enums.ClientVendorType;
import com.icolak.service.ClientVendorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/clientVendors")
public class ClientVendorController {

    private final ClientVendorService clientVendorService;

    public ClientVendorController(ClientVendorService clientVendorService) {
        this.clientVendorService = clientVendorService;
    }

    @GetMapping("/list")
    public String getClientVendors(Model model) {

        model.addAttribute("clientVendors", clientVendorService.listClientVendors());

        return "/clientVendor/clientVendor-list";
    }

    @GetMapping("/create")
    public String createClientVendors(Model model) {

        model.addAttribute("newClientVendor", new ClientVendorDTO());
        model.addAttribute("clientVendorTypes", ClientVendorType.values());
        model.addAttribute("countries", StaticConstants.COUNTRY_LIST);

        return "/clientVendor/clientVendor-create";
    }

    @PostMapping("/create")
    public String insertClientVendors(@Valid @ModelAttribute("newClientVendor") ClientVendorDTO clientVendorDTO,
                                      BindingResult bindingResult, Model model) {

        if (clientVendorService.isClientVendorNameExist(clientVendorDTO.getClientVendorName())) {
            bindingResult.rejectValue("clientVendorName", "", "This name already exists");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("clientVendorTypes", ClientVendorType.values());
            model.addAttribute("countries", StaticConstants.COUNTRY_LIST);
            return "/clientVendor/clientVendor-create";
        }

        clientVendorService.save(clientVendorDTO);

        return "redirect:/clientVendors/list";
    }

    @GetMapping("/update/{id}")
    public String editClientVendors(@PathVariable("id") Long id, Model model) {

        model.addAttribute("clientVendor", clientVendorService.findById(id));
        model.addAttribute("clientVendorTypes", ClientVendorType.values());
        model.addAttribute("countries", StaticConstants.COUNTRY_LIST);

        return "/clientVendor/clientVendor-update";
    }

    @PostMapping("/update/{id}")
    public String updateClientVendors(@Valid @ModelAttribute("clientVendor") ClientVendorDTO clientVendorDTO,
                                      BindingResult bindingResult, Model model) {

        if (clientVendorService.isClientVendorNameExistExceptCurrent(clientVendorDTO)) {
            bindingResult.rejectValue("clientVendorName", "", "This name already exists");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("clientVendorTypes", ClientVendorType.values());
            model.addAttribute("countries", StaticConstants.COUNTRY_LIST);

            return "/clientVendor/clientVendor-update";
        }

        clientVendorService.update(clientVendorDTO);

        return "redirect:/clientVendors/list";
    }
}
