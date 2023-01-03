package com.icolak.service.implementation;

import com.icolak.dto.RoleDTO;
import com.icolak.dto.UserDTO;
import com.icolak.entity.User;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.RoleRepository;
import com.icolak.service.RoleService;
import com.icolak.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final MapperUtil mapperUtil;
    private final UserService userService;

    public RoleServiceImpl(RoleRepository roleRepository, MapperUtil mapperUtil, UserService userService) {
        this.roleRepository = roleRepository;
        this.mapperUtil = mapperUtil;
        this.userService = userService;
    }

    @Override
    public RoleDTO findById(Long id) {
        return mapperUtil.convert(roleRepository.findById(id).orElseThrow(), new RoleDTO());
    }

    @Override
    public List<RoleDTO> getRoles() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO currentUserDTO = userService.findByUsername(username);
        User dbUser = mapperUtil.convert(currentUserDTO, new User());

        if (dbUser.getRole().getDescription().equals("Root User")) {
            return roleRepository.findByDescription("Admin").stream()
                    .map(role -> mapperUtil.convert(role, new RoleDTO()))
                    .collect(Collectors.toList());
        }

        if (dbUser.getRole().getDescription().equals("Admin")) {
            return roleRepository.findByDescriptionIsNot("Root User").stream()
                    .map(role -> mapperUtil.convert(role, new RoleDTO()))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
