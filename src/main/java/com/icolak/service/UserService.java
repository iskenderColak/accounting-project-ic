package com.icolak.service;

import com.icolak.dto.UserDTO;
import com.icolak.entity.Company;

import java.util.List;

public interface UserService {

    UserDTO findById(Long id);

    List<UserDTO> listAllUsers();

    UserDTO findByUsername(String username);

    void save(UserDTO userDTO);

    void makeUserDisableByCompany(Company company);

    void makeUserEnableByCompany(Company company);

    boolean isUsernameExist(String username);

    boolean isUsernameExistExceptCurrentUsername(String username);

    UserDTO update(UserDTO userDTO);

    void delete(Long id);
}
