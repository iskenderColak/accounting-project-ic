package com.icolak.converter;

import com.icolak.dto.RoleDTO;
import com.icolak.service.RoleService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoleDtoConverter implements Converter<String, RoleDTO> {

    private final RoleService roleService;

    public RoleDtoConverter(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public RoleDTO convert(String source) {

        if (source == null || source.isBlank()) {
            return null;
        }

        return roleService.findById(Long.parseLong(source));
    }
}
