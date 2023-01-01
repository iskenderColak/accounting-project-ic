package com.icolak.service.implementation;

import com.icolak.dto.UserDTO;
import com.icolak.entity.User;
import com.icolak.entity.common.UserPrincipal;
import com.icolak.repository.UserRepository;
import com.icolak.service.SecurityService;
import com.icolak.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;
    private final UserService userService;

    public SecurityServiceImpl(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserPrincipal(user);
    }

    @Override
    public UserDTO getLoggedInUser() {
        var currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByUsername(currentUsername);
    }
}
