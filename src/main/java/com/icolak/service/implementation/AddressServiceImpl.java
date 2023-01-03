package com.icolak.service.implementation;

import com.icolak.dto.AddressDTO;
import com.icolak.entity.Address;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.AddressRepository;
import com.icolak.service.AddressService;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final MapperUtil mapperUtil;

    public AddressServiceImpl(AddressRepository addressRepository, MapperUtil mapperUtil) {
        this.addressRepository = addressRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public AddressDTO save(AddressDTO addressDTO) {
        addressRepository.save(mapperUtil.convert(addressDTO, new Address()));
        return addressDTO;
    }
}
