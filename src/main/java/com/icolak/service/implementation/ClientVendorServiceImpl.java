package com.icolak.service.implementation;

import com.icolak.dto.ClientVendorDTO;
import com.icolak.dto.InvoiceDTO;
import com.icolak.entity.ClientVendor;
import com.icolak.entity.Company;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.ClientVendorRepository;
import com.icolak.service.ClientVendorService;
import com.icolak.service.InvoiceService;
import com.icolak.service.SecurityService;
import com.icolak.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientVendorServiceImpl implements ClientVendorService {

    private final ClientVendorRepository clientVendorRepository;
    private final MapperUtil mapperUtil;
    private final UserService userService;
    private final SecurityService securityService;
    private final InvoiceService invoiceService;

    public ClientVendorServiceImpl(ClientVendorRepository clientVendorRepository, MapperUtil mapperUtil, UserService userService, SecurityService securityService, InvoiceService invoiceService) {
        this.clientVendorRepository = clientVendorRepository;
        this.mapperUtil = mapperUtil;
        this.userService = userService;
        this.securityService = securityService;
        this.invoiceService = invoiceService;
    }

    @Override
    public ClientVendorDTO findById(Long id) {
        return mapperUtil.convert(clientVendorRepository.findById(id).orElseThrow(), new ClientVendorDTO());
    }

    @Override
    public List<ClientVendorDTO> listClientVendors() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        UserDTO currentUserDTO = userService.findByUsername(username);
//        User dbUser = mapperUtil.convert(currentUserDTO, new User());

        String companyTitle = securityService.getLoggedInUser().getCompany().getTitle();

        return clientVendorRepository.findAll().stream()
//                .filter(clientVendor -> clientVendor.getCompany().getTitle().equals(dbUser.getCompany().getTitle()))
                .filter(clientVendor -> clientVendor.getCompany().getTitle().equals(companyTitle))
                .map(clientVendor -> mapperUtil.convert(clientVendor, new ClientVendorDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(ClientVendorDTO clientVendorDTO) {

        ClientVendor clientVendor = mapperUtil.convert(clientVendorDTO, new ClientVendor());
        clientVendor.setCompany
                (mapperUtil.convert(securityService.getLoggedInUser().getCompany(), new Company()));

        clientVendorRepository.save(clientVendor);
    }

    @Override
    public ClientVendorDTO update(ClientVendorDTO clientVendorDTO) {

        save(clientVendorDTO);

        return findById(clientVendorDTO.getId());
    }

    @Override
    public boolean isClientVendorNameExist(String clientVendorName) {
        return clientVendorRepository.existsByClientVendorName(clientVendorName);
    }

    @Override
    public boolean isClientVendorNameExistExceptCurrent(ClientVendorDTO clientVendorDTO) {
        ClientVendor clientVendor = mapperUtil.convert(findById(clientVendorDTO.getId()), new ClientVendor());
        if (clientVendor.getClientVendorName().equals(clientVendorDTO.getClientVendorName().trim())) {
            return false;
        }
        return isClientVendorNameExist(clientVendorDTO.getClientVendorName());
    }

    @Override
    public void delete(Long id) throws IllegalAccessException {
        Optional<ClientVendor> clientVendor = clientVendorRepository.findById(id);
        if (clientVendor.isPresent()) {
            if (!(invoiceService.existsByClientVendorId(id))) {
                clientVendor.get().setIsDeleted(true);
                clientVendorRepository.save(clientVendor.get());
            } else {
                throw new IllegalAccessException("Cannot be deleted. Has invoice linked to Client/Vendor");
            }
        }
    }
}
