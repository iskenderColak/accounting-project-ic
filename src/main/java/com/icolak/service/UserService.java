package com.icolak.service;

import com.icolak.dto.UserDTO;
import com.icolak.entity.Company;

import java.util.List;

public interface UserService {

    UserDTO findById(Long id);

    List<UserDTO> listAllUsers();

    UserDTO findByUsername(String username);

    void save(UserDTO userDTO);

    void makeUserEnableFalseByCompany(Company company);

    void makeUserEnableTrueByCompany(Company company);
}
