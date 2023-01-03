package com.icolak.service;

import com.icolak.dto.RoleDTO;

import java.util.List;

public interface RoleService {

    RoleDTO findById(Long id);

    List<RoleDTO> getRoles();
}
