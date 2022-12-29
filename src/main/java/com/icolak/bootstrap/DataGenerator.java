package com.icolak.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class DataGenerator implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        // Using Locale class static method getISOCountries()
        // we extract a list of all 2-letter country codes
        // defined in ISO 3166. Can be used to create Locales.

        String[] countries = Locale.getISOCountries();

        // Loop each country
        for(int i = 0; i < countries.length; i++) {

            String country = countries[i];
            Locale locale = new Locale("en", country);

            // Get the country name by calling getDisplayCountry()
            String countryName = locale.getDisplayCountry();
            StaticConstants.COUNTRY_LIST.add(countryName);
        }
    }
}
