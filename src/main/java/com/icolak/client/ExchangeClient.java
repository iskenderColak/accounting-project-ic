package com.icolak.client;

import com.icolak.dto.currency.ExchangeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "https://api.freecurrencyapi.com", name = "EXCHANGE-RATES")
public interface ExchangeClient {
    @GetMapping("/v1/latest")
    ExchangeDTO getExchangeRates(@RequestParam(value = "apikey") String apikey);
}
