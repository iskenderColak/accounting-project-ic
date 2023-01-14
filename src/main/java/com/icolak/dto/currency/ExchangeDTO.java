package com.icolak.dto.currency;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("jsonschema2pojo")
@Data
public class ExchangeDTO {

    @JsonProperty("data")
    private DataDTO dataDTO;

}
