package com.icolak.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String username;
    private String password;
    private String confirmPassword;
    private String firstname;
    private String lastname;
    private String phone;
    private Boolean enabled;
    private RoleDTO role;
    private CompanyDTO company;
    private Boolean isOnlyAdmin; // (should be true if this user is only admin of any company.)

}
