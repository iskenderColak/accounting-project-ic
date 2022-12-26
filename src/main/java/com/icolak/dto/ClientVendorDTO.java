package com.icolak.dto;

import com.icolak.enums.ClientVendorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientVendorDTO {

    private Long id;
    private String clientVendorName;
    private String phone;
    private String website;
    private ClientVendorType clientVendorType;
    private AddressDTO address;
    private CompanyDTO company;

}
