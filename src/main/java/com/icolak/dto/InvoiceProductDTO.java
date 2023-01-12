package com.icolak.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceProductDTO {

    private Long id;

    @NotNull(message =  "Quantity is a required field." )
    @Min(value = 1, message = "Should be more than 0")
    @Max(value = 100, message = "Maximum order count is 100" )
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
