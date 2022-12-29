package com.icolak.service.implementation;

import com.icolak.dto.UserDTO;
import com.icolak.entity.Company;
import com.icolak.entity.User;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.UserRepository;
import com.icolak.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public UserDTO findById(Long id) {
        return null;
    }

    @Override
    public List<UserDTO> listAllUsers() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO currentUserDTO = findByUsername(username);
        User dbUser = mapperUtil.convert(currentUserDTO, new User());

        if (dbUser.getRole().getDescription().equals("Root User")) {
            return userRepository.findAll().stream()
                    .filter(user -> user.getRole().getDescription().equals("Admin"))
                    .map(user -> {
                        UserDTO dto = mapperUtil.convert(user, new UserDTO());
                        dto.setIsOnlyAdmin(false);
                        return dto;
                    }).collect(Collectors.toList());
        }

        return userRepository.findAll().stream()
                .filter(user -> user.getCompany().getTitle().equals(dbUser.getCompany().getTitle()))
                .map(user -> {
                    UserDTO dto = mapperUtil.convert(user, new UserDTO());
                    if (user.getRole().getDescription().equals("Admin")) {
                        dto.setIsOnlyAdmin(true);
                    } else {
                        dto.setIsOnlyAdmin(false);
                    }
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUsername(String username) {
        return mapperUtil.convert(userRepository.findByUsername(username), new UserDTO());
    }

    @Override
    public void save(UserDTO userDTO) {
        userRepository.save(mapperUtil.convert(userDTO, new User()));
    }

    @Override
    public void makeUserEnableFalseByCompany(Company company) {
        List<User> userList = userRepository.findAllByCompany(company);
        userList.forEach(user -> user.setEnabled(false));
        userRepository.saveAll(userList);
    }

    @Override
    public void makeUserEnableTrueByCompany(Company company) {
        List<User> userList = userRepository.findAllByCompany(company);
        userList.forEach(user -> user.setEnabled(true));
        userRepository.saveAll(userList);
    }
}
