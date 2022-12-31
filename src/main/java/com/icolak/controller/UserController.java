package com.icolak.controller;

import com.icolak.dto.UserDTO;
import com.icolak.service.CompanyService;
import com.icolak.service.RoleService;
import com.icolak.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final CompanyService companyService;

    public UserController(UserService userService, RoleService roleService, CompanyService companyService) {
        this.userService = userService;
        this.roleService = roleService;
        this.companyService = companyService;
    }

    @GetMapping("/list")
    public String getUsers(Model model) {

        model.addAttribute("users", userService.listAllUsers());

        return "/user/user-list";
    }

    @GetMapping("/create")
    public String createUser(Model model) {

        model.addAttribute("newUser", new UserDTO());
        model.addAttribute("userRoles", roleService.getRoles());
        model.addAttribute("companies", companyService.listAllCompanies());

        return "/user/user-create";
    }

    @PostMapping("/create")
    public String insertUser(@Valid @ModelAttribute("newUser") UserDTO user,
                                BindingResult bindingResult, Model model) {

        if (userService.isUsernameExist(user.getUsername())) {
            bindingResult.rejectValue("username", " ", "This username already exists");
            return "/user/user-create";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("userRoles", roleService.getRoles());
            model.addAttribute("companies", companyService.listAllCompanies());
            return "/user/user-create";
        }

        userService.save(user);

        return "redirect:/users/list";
    }
}
