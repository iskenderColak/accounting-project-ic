package com.icolak.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank(message = "Required field")
    private String username;

    @NotBlank(message = "Required field")
    private String password;

    @Size(min = 2, max = 50, message = "Password must be between 2 and 50 characters long.")
    private String confirmPassword;

    @NotBlank(message = "Required field")
    @Size(min = 2, max = 50, message = "First Name must be between 2 and 50 characters long.")
    private String firstname;

    @NotBlank(message = "Required field")
    @Size(min = 2, max = 50, message = "Last Name must be between 2 and 50 characters long.")
    private String lastname;

    @NotBlank(message = "Required field")
    private String phone;

    private Boolean enabled;

    @NotNull(message = "Please select a Role")
    @Valid
    private RoleDTO role;

    @NotNull(message = "Please select a Customer")
    @Valid
    private CompanyDTO company;

    private Boolean isOnlyAdmin; // (should be true if this user is only admin of any company.)

}
