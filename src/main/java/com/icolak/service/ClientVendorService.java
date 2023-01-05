package com.icolak.service;

import com.icolak.dto.ClientVendorDTO;
import com.icolak.enums.ClientVendorType;

import java.util.List;

public interface ClientVendorService {

    ClientVendorDTO findById(Long id);
    List<ClientVendorDTO> listClientVendors();
    void save(ClientVendorDTO clientVendorDTO);
    ClientVendorDTO update(ClientVendorDTO clientVendorDTO);
    boolean isClientVendorNameExist(String clientVendorName);
    boolean isClientVendorNameExistExceptCurrent(ClientVendorDTO clientVendorDTO);
    void delete(Long id) throws IllegalAccessException;
    List<ClientVendorDTO> listClientVendorsByType(ClientVendorType type);
}
