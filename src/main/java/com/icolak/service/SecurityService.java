package com.icolak.service;

import com.icolak.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface SecurityService extends UserDetailsService {

    UserDTO getLoggedInUser();

}
