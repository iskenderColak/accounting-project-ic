package com.icolak.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProductDTO {

    private Long id;
    private Integer quantity;
    private BigDecimal price;
    private Integer tax;
    private BigDecimal total;
    private BigDecimal profitLoss;
    private Integer remainingQuantity;
    private InvoiceDTO invoice;
    private ProductDTO product;

    public BigDecimal getTotal() {
        BigDecimal withoutTax = BigDecimal.valueOf(quantity).multiply(price);
        BigDecimal taxValue = BigDecimal.valueOf(tax).multiply(withoutTax).divide(BigDecimal.valueOf(100L), RoundingMode.HALF_EVEN);
        return withoutTax.add(taxValue);
    }

}
