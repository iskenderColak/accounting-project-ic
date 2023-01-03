package com.icolak.service;

import com.icolak.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    ProductDTO findById(Long id);

    List<ProductDTO> listAllProducts();

    boolean isExistByCategoryId(Long id);

    void save(ProductDTO productDTO);

    ProductDTO update(ProductDTO productDTO);

    void delete(Long id);
}
