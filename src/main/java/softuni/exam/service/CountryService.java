package softuni.exam.service;

import softuni.exam.models.entity.Country;

import java.io.IOException;
import java.util.Optional;

// TODO: Implement all methods
public interface CountryService {

    boolean areImported();

    String readCountriesFromFile() throws IOException;

    String importCountries() throws IOException;

    void saveAddedVolcanoInCountry(Country country);

    Optional<Country> getCountryById(Long country);

//    Optional<Country> getCountryById(Long countryId);
//
//    void saveAddedVolcanoInCountry(Country country);
}
