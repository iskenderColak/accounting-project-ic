package com.icolak.service;

import com.icolak.dto.InvoiceProductDTO;
import com.icolak.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    ProductDTO findById(Long id);

    boolean isExistByCategoryId(Long id);

    void save(ProductDTO productDTO);

    ProductDTO update(ProductDTO productDTO);

    void delete(Long id);

    List<ProductDTO> listAllProductsByCompany();

    boolean isStockEnough(InvoiceProductDTO invoiceProductDTO);

    ProductDTO findByName(String name);

    void setStockAfterSelling(Long productId, int quantitySold);
}