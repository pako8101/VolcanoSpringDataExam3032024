package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.VolcanologistSeedRootDto;
import softuni.exam.models.entity.Volcanologist;
import softuni.exam.repository.VolcanoRepository;
import softuni.exam.repository.VolcanologistRepository;
import softuni.exam.service.VolcanoService;
import softuni.exam.service.VolcanologistService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParserImpl;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class VolcanologistServiceImpl implements VolcanologistService {
    private final XmlParserImpl xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final VolcanologistRepository volcanologistRepository;
    private final VolcanoService volcanoService;
    private final VolcanoRepository volcanoRepository;
    private static final String PATH ="src/main/resources/files/xml/volcanologists.xml";

    public VolcanologistServiceImpl(XmlParserImpl xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper, VolcanologistRepository volcanologistRepository, VolcanoService volcanoService, VolcanoRepository volcanoRepository) {
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.volcanologistRepository = volcanologistRepository;
        this.volcanoService = volcanoService;
        this.volcanoRepository = volcanoRepository;
    }


    @Override
    public boolean areImported() {
        return this.volcanologistRepository.count()>0;
    }

    @Override
    public String readVolcanologistsFromFile() throws IOException {
        return Files.readString(Path.of(PATH));
    }

    @Override
    public String importVolcanologists() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(PATH, VolcanologistSeedRootDto.class)
                .getVolcanologistSeedDtos()
                .stream()
                .filter(volcanologistSeedDto -> {
                    boolean isValid = validationUtil.isValid(volcanologistSeedDto);

                  Optional<Volcanologist> optVolcanologist = volcanologistRepository
                            .findByFirstNameAndLastName(volcanologistSeedDto.getFirstName(),volcanologistSeedDto.getLastName());
                    if (optVolcanologist.isPresent()){
                        isValid=false;
                    }

                    sb.append(isValid ? String.format(String.format("Successfully imported volcanologist %s %s\n",
                   volcanologistSeedDto.getFirstName(),volcanologistSeedDto.getLastName()))
                                    : "Invalid volcanologist")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(volcanologistSeedDto -> {
                    Volcanologist volcanologist = modelMapper.map(volcanologistSeedDto, Volcanologist.class);
                    volcanologist.setExploringVolcano(volcanoService.findVolcanoById(volcanologistSeedDto.getExploringVolcano()));

                    return volcanologist;
                })
                .forEach(volcanologistRepository::saveAndFlush);


        return sb.toString().trim();
    }


//        JAXBContext jaxbContext = JAXBContext.newInstance(VolcanologistSeedRootDto.class);
//        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//        VolcanologistSeedRootDto volcanologistSeedRootDto =
//                (VolcanologistSeedRootDto) unmarshaller.unmarshal(new File(PATH));
//
//        for (VolcanologistSeedDto volcanologistSeedDto : volcanologistSeedRootDto.getVolcanologistSeedDtos()) {
//            Optional<Volcanologist> optionalVolcanologist =
//                    this.volcanologistRepository
//                            .findByFirstNameAndLastName(volcanologistSeedDto.getFirstName(),
//                                    volcanologistSeedDto.getLastName());
//            Optional<Volcano>optionalVolcano = this.volcanoRepository.findById(volcanologistSeedDto.getExploringVolcano());
//            if (!this.validationUtil.isValid(volcanologistSeedDto)||
//                    optionalVolcanologist.isPresent() || optionalVolcano.isEmpty()){
//                sb.append("Invalid volcanologist!\n");
//                continue;
//            }
//            Volcanologist volcanologist =
//                    this.modelMapper.map(volcanologistSeedDto,Volcanologist.class);
//            volcanologist.setExploringVolcano(optionalVolcano.get());
//            this.volcanologistRepository.saveAndFlush(volcanologist);
//
//            sb.append(String.format("Successfully imported volcanologist %s %s\n",
//                    volcanologist.getFirstName(),volcanologist.getLastName()));
//        }
//        return sb.toString().trim();
//    }
    }
