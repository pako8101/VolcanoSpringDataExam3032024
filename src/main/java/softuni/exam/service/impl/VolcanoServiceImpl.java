package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.VolcanoesSeedDto;
import softuni.exam.models.entity.Country;
import softuni.exam.models.entity.Volcano;
import softuni.exam.models.entity.Volcanologist;
import softuni.exam.repository.CountryRepository;
import softuni.exam.repository.VolcanoRepository;
import softuni.exam.service.CountryService;
import softuni.exam.service.VolcanoService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VolcanoServiceImpl implements VolcanoService {
    private final VolcanoRepository volcanoRepository;
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final CountryService countryService;
    private final static String PATH = "src/main/resources/files/json/volcanoes.json";

    public VolcanoServiceImpl(VolcanoRepository volcanoRepository, CountryRepository countryRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, CountryService countryService) {
        this.volcanoRepository = volcanoRepository;
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.countryService = countryService;
    }


    @Override
    public boolean areImported() {
        return this.volcanoRepository.count() > 0;
    }

    @Override
    public String readVolcanoesFileContent() throws IOException {
        return Files.readString(Path.of(PATH));
    }

    @Override
    public String importVolcanoes() throws IOException {
        StringBuilder sb = new StringBuilder();
        VolcanoesSeedDto[] volcanoesSeedDtos = this.gson.
                fromJson(readVolcanoesFileContent(), VolcanoesSeedDto[].class);

        Arrays.stream(volcanoesSeedDtos)
                .filter(volcanoesSeedDto -> {
                    boolean isValid = validationUtil.isValid(volcanoesSeedDto);
                    Optional<Volcano> optVolcano =
                            this.volcanoRepository.findByName(volcanoesSeedDto.getName());

                    if (optVolcano.isPresent()) {
                        isValid = false;
                    }


                    sb.append(isValid
                                    ? String.format("Successfully imported volcano %s of type %s"
                                    , volcanoesSeedDto.getName(), volcanoesSeedDto.getVolcanoType()) :
                                    "Invalid volcano")
                            .append(System.lineSeparator());
                    return isValid;

                }).
                map(volcanoesSeedDto -> {
                    Volcano volcano = this.modelMapper.map(volcanoesSeedDto, Volcano.class);
                    Country country = countryService.getCountryById(volcanoesSeedDto.getCountry()).orElse(null);
                    country.getVolcanoes().add(volcano);
                    countryService.saveAddedVolcanoInCountry(country);
                    volcano.setCountry(country);
                    return volcano;
                })
                .forEach(volcanoRepository::saveAndFlush);


//        for (VolcanoesSeedDto volcanoesSeedDto : volcanoesSeedDtos) {
//            Optional<Volcano> optional = this.volcanoRepository.findByName(volcanoesSeedDto.getName());
//            Optional<Country> optCountry =
//                    this.countryRepository.findById(volcanoesSeedDto.getCountry());
//
////                    if (!isValid|| optVolcano.isPresent() || optCountry.isEmpty()) {
////                        sb.append("Invalid volcano\n");
////
////                    }
//            if (!this.validationUtil.isValid(volcanoesSeedDto) || optional.isPresent() || optCountry.isEmpty()) {
//                sb.append("Invalid volcano!\n");
//                continue;
//            }
//            System.out.println();
//
//            Volcano volcano = this.modelMapper.map(volcanoesSeedDto, Volcano.class);
//
//            volcano.setCountry(optCountry.get());
//            this.volcanoRepository.saveAndFlush(volcano);
//            sb.append(String.format("Successfully imported volcano %s of type %s\n"
//                    , volcano.getName(), volcano.getVolcanoType()));
//        }

        return sb.toString().trim();
    }

    @Override
    public Volcano findVolcanoById(Long volcanoId) {
        return this.volcanoRepository.findById(volcanoId).orElse(null);
    }

    @Override
    public void addAndSaveAddedVolcano(Volcano volcano, Volcanologist volcanologist) {
        volcano.getVolcanologists().add(volcanologist);
        volcanoRepository.save(volcano);
    }

    @Override
    public String exportVolcanoes() {
        return this.volcanoRepository.findAllByActiveIsTrueAndElevationGreaterThanAndLastEruptionNotNullOrderByElevationDesc(3000)
                .stream()
                .map(v -> String.format("Volcano: %s\n" +
                                "  *Located in: %s\n" +
                                "  **Elevation: %d\n" +
                                "  ***Last eruption on: %s\n",
                        v.getName(), v.getCountry(), v.getElevation()
                        , v.getLastEruption()
                )).collect(Collectors.joining());
    }
}