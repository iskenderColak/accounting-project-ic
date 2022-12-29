package com.icolak.converter;

import com.icolak.dto.CompanyDTO;
import com.icolak.service.CompanyService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CompanyDtoConverter implements Converter<String, CompanyDTO> {

    private final CompanyService companyService;

    public CompanyDtoConverter(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    public CompanyDTO convert(String source) {
        return companyService.findById(Long.parseLong(source));
    }
}
