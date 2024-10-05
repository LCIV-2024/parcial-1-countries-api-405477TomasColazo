package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.model.enums.CONTINENT;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class CountryController {
    @Autowired
    private CountryService countryService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/countries/")
    public ResponseEntity<List<CountryDTO>> getPaises(@RequestParam(value = "code",required = false)String code ,
                                                      @RequestParam(value = "name",required = false) String name) {

        if(code!=null){
            return ResponseEntity.ok(countryService.getAllCountriesByCode(code));
        }
        if(name!=null){
            return ResponseEntity.ok(countryService.getAllCountriesByName(name));
        }
        return ResponseEntity.ok(countryService.getAllCountriesDTO());
    }
    @GetMapping("/countries/{continent}")
    public ResponseEntity<List<CountryDTO>> getPaisesByContinet(@PathVariable String continent){
        return ResponseEntity.ok(countryService.getAllCountriesByContinent(continent));
    }
    @GetMapping("/countries/{language}")
    public ResponseEntity<List<CountryDTO>> getPaisesByLanguage(@PathVariable String language){
        return ResponseEntity.ok(countryService.getAllCountriesByLanguage(language));
    }
    @GetMapping("/countries/most-borders")
    public ResponseEntity<CountryDTO> getPaisesByBorderes(){
        return ResponseEntity.ok(countryService.getCountryMostBorders());
    }
    @PostMapping("countries")
    public ResponseEntity<List<CountryDTO>> postPaises(@RequestBody int amountOfCountryToSave){
        return ResponseEntity.ok(countryService.postCountries(amountOfCountryToSave));
    }
}