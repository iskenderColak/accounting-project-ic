package com.icolak.service.implementation;

import com.icolak.dto.CompanyDTO;
import com.icolak.entity.Company;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.CompanyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    CompanyRepository companyRepository;
    @Mock
    MapperUtil mapperUtil;
    @InjectMocks
    CompanyServiceImpl companyService;


    @Test
    @DisplayName("Testing findById()")
    void findById() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(new Company()));
        when(mapperUtil.convert(any(Company.class), any(CompanyDTO.class))).thenReturn(new CompanyDTO());

        CompanyDTO companyDTO = companyService.findById(anyLong());

        InOrder inOrder = inOrder(companyRepository, mapperUtil);
        inOrder.verify(companyRepository).findById(anyLong());
        inOrder.verify(mapperUtil).convert(any(Company.class), any(CompanyDTO.class));
        Assertions.assertTrue(companyService.findById(anyLong()) instanceof CompanyDTO);
        assertNotNull(companyDTO);

    }
}