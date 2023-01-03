package com.icolak.service.implementation;

import com.icolak.dto.ProductDTO;
import com.icolak.mapper.MapperUtil;
import com.icolak.repository.ProductRepository;
import com.icolak.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MapperUtil mapperUtil;

    public ProductServiceImpl(ProductRepository productRepository, MapperUtil mapperUtil) {
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public ProductDTO findById(Long id) {
        return mapperUtil.convert(productRepository.findById(id).orElseThrow(), new ProductDTO());
    }

    @Override
    public List<ProductDTO> listAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> mapperUtil.convert(product, new ProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isExistByCategoryId(Long id) {
        return productRepository.existsByCategoryId(id);
    }
}
