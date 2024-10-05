package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.model.Country;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class CountryService {

        //private final CountryRepository countryRepository;

        @Autowired
        private RestTemplate restTemplate;

        public List<CountryDTO> getAllCountriesDTO() {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                List<Country> countries = new ArrayList<>();
                        List<CountryDTO> countryDTOS = new ArrayList<>();
                response.forEach(i -> countries.add(mapToCountry(i)));
                countries.forEach(i -> countryDTOS.add(mapToDTO(i)));
                return countryDTOS;
        }
        public List<Country> getAllCountries() {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                List<Country> countries = new ArrayList<>();
                List<CountryDTO> countryDTOS = new ArrayList<>();
                response.forEach(i -> countries.add(mapToCountry(i)));
                countries.forEach(i -> countryDTOS.add(mapToDTO(i)));
                return countries;
        }
        /**
         * Agregar mapeo de campo cca3 (String)
         * Agregar mapeo campos borders ((List<String>))
         */
        private Country mapToCountry(Map<String, Object> countryData) {
                Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");
                String cca3 =  (String)countryData.get("cca3");
                List<String> borders = (List<String>)countryData.get("borders");
                String name = (String) nameData.get("common");
                Number number =(Number) countryData.get("population");
                Double area = (Double) countryData.get("area");
                String region = (String) countryData.get("region");
                List<String> continents = (List<String>) countryData.get("continents");
                Map<String,String> languages = (Map<String, String>) countryData.get("languages");

                Country country = new Country();

                if(name!=null) country.setName(name);
                if (number!=null) country.setPopulation(number);
                if(area!=null)country.setArea(area.doubleValue());
                if (region!=null)country.setRegion(region);
                if (languages!=null) country.setLanguages(languages);
                if(borders != null) country.setBorders(borders);
                if(cca3!=null) country.setCode(cca3);
                if (continents!=null) country.setContinents(continents);
                return country;
        }


        private CountryDTO mapToDTO(Country country) {
                return new CountryDTO(country.getCode(), country.getName());
        }

        public List<CountryDTO> getAllCountriesByCode(String code) {
                List<CountryDTO> countryDTOS = getAllCountriesDTO();
                List<CountryDTO> countryDTOS2 = new ArrayList<>();
                countryDTOS.forEach(i -> {
                        if (Objects.equals(i.getCode(), code)) {
                                countryDTOS2.add(i);
                        }
                });
                return countryDTOS2;
        }

        public List<CountryDTO> getAllCountriesByName(String name) {
                List<CountryDTO> countryDTOS = getAllCountriesDTO();
                List<CountryDTO> countryDTOS2 = new ArrayList<>();
                countryDTOS.forEach(i -> {
                        if (Objects.equals(i.getName(), name)) {
                                countryDTOS2.add(i);
                        }
                });
                return countryDTOS2;
        }

        public List<CountryDTO> getAllCountriesByContinent(String continent) {
               List<Country> countries = getAllCountries();
                List<CountryDTO> countryDTOS2 = new ArrayList<>();
                countries.forEach(i -> {
                        if (i.getContinents().contains(continent)){
                                countryDTOS2.add(mapToDTO(i));
                        }
                });
                return countryDTOS2;
        }

        public List<CountryDTO> getAllCountriesByLanguage(String language) {
                List<Country> countries = getAllCountries();
                List<CountryDTO> countryDTOS2 = new ArrayList<>();
                countries.forEach(i -> {
                        if (i.getLanguages().containsValue(language)){
                                countryDTOS2.add(mapToDTO(i));
                        }
                });
                return countryDTOS2;
        }

        public CountryDTO getCountryMostBorders() {
                List<Country> countries = getAllCountries();
                AtomicInteger x = new AtomicInteger();
                AtomicReference<Country> country = new AtomicReference<Country>();
                countries.forEach(i->{
                        if (i.getBorders().size() > x.get()){
                                x.set(i.getBorders().size());
                                country.set(i);
                        }
                });
                return mapToDTO(country.get());
        }

        public List<CountryDTO> postCountries(int amountOfCountryToSave) {
                List<CountryDTO> countries = getAllCountriesDTO();
                List<CountryDTO> countryDTOS2 = new ArrayList<>();
                for (int i = 0; i < amountOfCountryToSave; i++) {
                        countryDTOS2.add(countries.get(i));
                }
                return countryDTOS2;
        }
}