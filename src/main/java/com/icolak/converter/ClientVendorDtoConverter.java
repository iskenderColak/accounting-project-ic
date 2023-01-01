package com.icolak.converter;

import com.icolak.dto.ClientVendorDTO;
import com.icolak.service.ClientVendorService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClientVendorDtoConverter implements Converter<String, ClientVendorDTO> {

    private final ClientVendorService clientVendorService;

    public ClientVendorDtoConverter(ClientVendorService clientVendorService) {
        this.clientVendorService = clientVendorService;
    }


    @Override
    public ClientVendorDTO convert(String source) {

        if (source == null || source.isBlank()) {
            return null;
        }

        return clientVendorService.findById(Long.parseLong(source));
    }
}
