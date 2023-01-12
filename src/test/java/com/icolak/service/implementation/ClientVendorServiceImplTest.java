package com.icolak.service.implementation;

import com.icolak.dto.ClientVendorDTO;
import com.icolak.entity.ClientVendor;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.ClientVendorRepository;
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
class ClientVendorServiceImplTest {

    @Mock
    ClientVendorRepository clientVendorRepository;
    @Mock
    MapperUtil mapperUtil;
    @InjectMocks
    ClientVendorServiceImpl clientVendorService;

    @Test
    @DisplayName("findById()")
    void findById_Test() {

        when(clientVendorRepository.findById(anyLong())).thenReturn(Optional.of(new ClientVendor()));
        when(mapperUtil.convert(any(ClientVendor.class), any(ClientVendorDTO.class))).thenReturn(new ClientVendorDTO());

        ClientVendorDTO clientVendorDTO = clientVendorService.findById(anyLong());

        InOrder inOrder = inOrder(clientVendorRepository, mapperUtil);
        inOrder.verify(clientVendorRepository).findById(anyLong());
        inOrder.verify(mapperUtil).convert(any(ClientVendor.class), any(ClientVendorDTO.class));
        assertNotNull(clientVendorDTO);
    }

}