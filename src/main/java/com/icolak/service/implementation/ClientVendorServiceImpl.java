package com.icolak.service.implementation;

import com.icolak.dto.ClientVendorDTO;
import com.icolak.dto.UserDTO;
import com.icolak.entity.User;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.ClientVendorRepository;
import com.icolak.service.ClientVendorService;
import com.icolak.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientVendorServiceImpl implements ClientVendorService {

    private final ClientVendorRepository clientVendorRepository;
    private final MapperUtil mapperUtil;

    private final UserService userService;

    public ClientVendorServiceImpl(ClientVendorRepository clientVendorRepository, MapperUtil mapperUtil, UserService userService) {
        this.clientVendorRepository = clientVendorRepository;
        this.mapperUtil = mapperUtil;
        this.userService = userService;
    }

    @Override
    public ClientVendorDTO findById(Long id) {
        return mapperUtil.convert(clientVendorRepository.findById(id).orElseThrow(), new ClientVendorDTO());
    }

    @Override
    public List<ClientVendorDTO> listClientVendors() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO currentUserDTO = userService.findByUsername(username);
        User dbUser = mapperUtil.convert(currentUserDTO, new User());
        return clientVendorRepository.findAll().stream()
                .filter(clientVendor -> clientVendor.getCompany().getTitle().equals(dbUser.getCompany().getTitle()))
                .map(clientVendor -> mapperUtil.convert(clientVendor, new ClientVendorDTO()))
                .collect(Collectors.toList());
    }
}
