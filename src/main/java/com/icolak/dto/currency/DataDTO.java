package com.icolak.dto.currency;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class DataDTO {
    @JsonProperty("EUR")
    private Double euro;

    @JsonProperty("GBP")
    private Double britishPound;

    @JsonProperty("CAD")
    private Double canadianDollar;

    @JsonProperty("JPY")
    private Double japaneseYen;

    @JsonProperty("INR")
    private Double indianRupee;

    @JsonProperty("TRY")
    private Double turkishLira;
}
/*
    @JsonProperty("USD")
    private Integer usd;

 */


