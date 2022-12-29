package com.icolak.dto;

import com.icolak.enums.CompanyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {

    private Long id;

    @NotBlank(message = "Required field")
    @Size(min = 2, max = 50, message = "Size should be between 2 and 50")
    private String title;

    @NotBlank(message = "Required field")
    @Size(min = 2, max = 50, message = "Size should be between 2 and 50")
    private String phone;

    @NotBlank(message = "Required field")
    private String website;

    @NotNull(message = "Required field")
    private CompanyStatus companyStatus;

    @NotNull(message = "Required field")
    private AddressDTO address;

}
