package com.icolak.service;

import com.icolak.dto.ClientVendorDTO;

import java.util.List;

public interface ClientVendorService {

    ClientVendorDTO findById(Long id);
    List<ClientVendorDTO> listClientVendors();
    void save(ClientVendorDTO clientVendorDTO);
    ClientVendorDTO update(ClientVendorDTO clientVendorDTO);
}
