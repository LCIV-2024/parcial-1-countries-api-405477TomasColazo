package ar.edu.utn.frc.tup.lciii.service;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.*;


import static org.mockito.Mockito.*;
@SpringBootTest
class CountryServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CountryService countryService;

    private String url;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        url= "https://restcountries.com/v3.1/all";

    }
    private List<Map<String, Object>> genericResponse() {
        Map<String, Object> countryMap = createCountryMap("ESP", "Spain", Arrays.asList("FRA", "PRT", "AND"));
        return Collections.singletonList(countryMap);
    }
    @Test
    void getAllCountriesDTO_ReturnListCountryDTO() {

        List<Map<String, Object>> response = genericResponse();
        when(restTemplate.getForObject(url, List.class)).thenReturn(response);


        List<CountryDTO> result = countryService.getAllCountriesDTO();


        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ESP", result.get(0).getCode());
        assertEquals("Spain", result.get(0).getName());
        verify(restTemplate).getForObject(url, List.class);
    }

    @Test
    void getAllCountriesByCode_ReturnFilteredList() {

        List<Map<String, Object>> mockResponse = genericResponse();
        when(restTemplate.getForObject(url, List.class)).thenReturn(mockResponse);


        List<CountryDTO> result = countryService.getAllCountriesByCode("ESP");


        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ESP", result.get(0).getCode());
    }

    @Test
    void getAllCountriesByContinent_ReturnFilteredList() {

        List<Map<String, Object>> mockResponse = genericResponse();
        when(restTemplate.getForObject(url, List.class)).thenReturn(mockResponse);


        List<CountryDTO> result = countryService.getAllCountriesByContinent("Europe");


        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Spain", result.get(0).getName());
    }

    @Test
    void getAllCountriesByLanguage_ReturnFilteredList() {

        List<Map<String, Object>> mockResponse = genericResponse();
        when(restTemplate.getForObject(url, List.class)).thenReturn(mockResponse);

        List<CountryDTO> result = countryService.getAllCountriesByLanguage("Spanish");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Spain", result.get(0).getName());
    }

    @Test
    void getCountryMostBorders_ReturnCountryWithMostBorders() {

        List<Map<String, Object>> mockResponse = Arrays.asList(
                createCountryMap("ESP", "Spain", Arrays.asList("FRA", "PRT", "AND")),
                createCountryMap("FRA", "France", Arrays.asList("ESP", "AND"))
        );
        when(restTemplate.getForObject(url, List.class)).thenReturn(mockResponse);


        CountryDTO result = countryService.getCountryMostBorders();


        assertNotNull(result);
        assertEquals("ESP", result.getCode());
    }



    private Map<String, Object> createCountryMap(String code, String name, List<String> borders) {
        Map<String, Object> countryMap = new HashMap<>();
        Map<String, Object> nameMap = new HashMap<>();
        nameMap.put("common", name);

        countryMap.put("name", nameMap);
        countryMap.put("cca3", code);
        countryMap.put("borders", borders);
        countryMap.put("population", 47000000);
        countryMap.put("area", 505990.0);
        countryMap.put("region", "Europe");
        countryMap.put("continents", Collections.singletonList("Europe"));

        Map<String, String> languages = new HashMap<>();
        languages.put("spa", "Spanish");
        countryMap.put("languages", languages);

        return countryMap;
    }
}