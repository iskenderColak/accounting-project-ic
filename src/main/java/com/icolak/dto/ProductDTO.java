package com.icolak.dto;

import com.icolak.enums.ProductUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Long id;
    @NotBlank(message = "Required field")
    @Size(min = 2, max = 50, message = "Product name should be between 2 and 50")
    private String name;

    private Integer quantityInStock;

    @NotNull(message = "Required field")
    @Min(value = 1, message = "Should be minimum 1")
    private Integer lowLimitAlert;

    @NotNull(message = "Required field")
    private ProductUnit productUnit;

    @NotNull(message = "Required field")
    private CategoryDTO category;

}
