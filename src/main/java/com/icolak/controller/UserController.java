package com.icolak.controller;

import com.icolak.dto.UserDTO;
import com.icolak.entity.User;
import com.icolak.service.CompanyService;
import com.icolak.service.RoleService;
import com.icolak.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
            model.addAttribute("userRoles", roleService.getRoles());
            model.addAttribute("companies", companyService.listAllCompanies());
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

    @GetMapping("/update/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {

        model.addAttribute("user", userService.findById(id));
        model.addAttribute("userRoles", roleService.getRoles());
        model.addAttribute("companies", companyService.listAllCompanies());

        return "user/user-update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@Valid @ModelAttribute("user") UserDTO userDTO,
                                BindingResult bindingResult, Model model) {

        if (userService.isUsernameExistExceptCurrentUsername(userDTO)) {
            bindingResult.rejectValue("username", " ", "This username already exists");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("userRoles", roleService.getRoles());
            model.addAttribute("companies", companyService.listAllCompanies());
            return "user/user-update";
        }

        userService.update(userDTO);

        return "redirect:/users/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {

        userService.delete(id);

        return "redirect:/users/list";
    }
}
