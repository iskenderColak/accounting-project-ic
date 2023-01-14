package com.icolak.client;

import com.icolak.dto.country.CountryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://country-codes1.p.rapidapi.com/countrycodes", name = "COUNTRY-LIST")
public interface CountryClient {
    @GetMapping
    CountryDTO getCountryList(@RequestHeader(value = "X-RapidAPI-Key") String key);
}
