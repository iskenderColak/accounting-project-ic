
package com.icolak.dto.country;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "totals",
    "countryCodes"
})
@Generated("jsonschema2pojo")
public class Data {

    @JsonProperty("totals")
    private Integer totals;
    @JsonProperty("countryCodes")
    private List<CountryCode> countryCodes = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("totals")
    public Integer getTotals() {
        return totals;
    }

    @JsonProperty("totals")
    public void setTotals(Integer totals) {
        this.totals = totals;
    }

    @JsonProperty("countryCodes")
    public List<CountryCode> getCountryCodes() {
        return countryCodes;
    }

    @JsonProperty("countryCodes")
    public void setCountryCodes(List<CountryCode> countryCodes) {
        this.countryCodes = countryCodes;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
