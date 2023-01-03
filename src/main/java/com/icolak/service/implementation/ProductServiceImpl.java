package com.icolak.service.implementation;

import com.icolak.dto.ProductDTO;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.ProductRepository;
import com.icolak.service.ProductService;
import com.icolak.service.SecurityService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;

    public ProductServiceImpl(ProductRepository productRepository, MapperUtil mapperUtil, SecurityService securityService) {
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
    }

    @Override
    public ProductDTO findById(Long id) {
        return mapperUtil.convert(productRepository.findById(id).orElseThrow(), new ProductDTO());
    }

    @Override
    public List<ProductDTO> listAllProducts() {
        return productRepository.findAll(Sort.by("category", "name")).stream()
                .filter(product -> product.getCategory().getCompany().getId().equals(securityService.getLoggedInUser().getCompany().getId()))
                .map(product -> mapperUtil.convert(product, new ProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isExistByCategoryId(Long id) {
        return productRepository.existsByCategoryId(id);
    }
}
