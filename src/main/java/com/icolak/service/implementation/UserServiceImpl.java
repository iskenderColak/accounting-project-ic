package com.icolak.service.implementation;

import com.icolak.dto.UserDTO;
import com.icolak.entity.Company;
import com.icolak.entity.User;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.UserRepository;
import com.icolak.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO findById(Long id) {
        return mapperUtil.convert(userRepository.findById(id).orElseThrow(), new UserDTO());
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
                    dto.setIsOnlyAdmin(user.getRole().getDescription().equals("Admin"));
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUsername(String username) {
        return mapperUtil.convert(userRepository.findByUsername(username), new UserDTO());
    }

    @Override
    public void save(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setEnabled(true);
        userRepository.save(mapperUtil.convert(userDTO, new User()));
    }

    @Override
    public void makeUserDisableByCompany(Company company) {
        List<User> userList = userRepository.findAllByCompany(company);
        userList.forEach(user -> user.setEnabled(false));
        userRepository.saveAll(userList);
    }

    @Override
    public void makeUserEnableByCompany(Company company) {
        List<User> userList = userRepository.findAllByCompany(company);
        userList.forEach(user -> user.setEnabled(true));
        userRepository.saveAll(userList);
    }

    @Override
    public boolean isUsernameExist(String username) {

        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean isUsernameExistExceptCurrentUsername(UserDTO userDTO) {

        User user = mapperUtil.convert(findById(userDTO.getId()), new User());
        if (user.getUsername().equals(userDTO.getUsername())) {
            return false;
        }
        return isUsernameExist(user.getUsername());
    }

    @Override
    public UserDTO update(UserDTO userDTO) {

        userRepository.save(mapperUtil.convert(userDTO, new User()));

        return findById(userDTO.getId());
    }

    @Override
    public void delete(Long id) {

        User user = userRepository.findById(id).orElseThrow();
        user.setIsDeleted(true);
        user.setUsername(user.getUsername() + " " + user.getId());
        userRepository.save(user);
    }
}
