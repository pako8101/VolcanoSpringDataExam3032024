package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CountrySeedDto;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final  static  String PATH ="src/main/resources/files/json/countries.json";
@Autowired
    public CountryServiceImpl(CountryRepository countryRepository, ModelMapper modelMapper,
                              ValidationUtil validationUtil, Gson gson) {
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.countryRepository.count()>0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        return Files.readString(Path.of(PATH));
    }

    @Override
    public String importCountries() throws IOException {
        StringBuilder sb = new StringBuilder();
        CountrySeedDto[]countrySeedDtos =
                this.gson.fromJson(new FileReader(PATH),
                        CountrySeedDto[].class);
        for (CountrySeedDto seedDto : countrySeedDtos) {
            Optional<Country> optional =
                    this.countryRepository.findByName(seedDto.getName());
            if (!this.validationUtil.isValid(seedDto)|| optional.isPresent()){
                sb.append("Invalid country\n");
                continue;
            }
            Country country = this.modelMapper.map(seedDto, Country.class);
            this.countryRepository.saveAndFlush(country);
            sb.append(String.format("Successfully imported country %s - %s\n",country.getName(),country.getCapital()));
        }



        return sb.toString().trim();
    }

    @Override
    public void saveAddedVolcanoInCountry(Country country) {
        countryRepository.save(country);
    }

    @Override
    public Optional<Country> getCountryById(Long countryId) {
        return countryRepository.findById(countryId);
    }

}
