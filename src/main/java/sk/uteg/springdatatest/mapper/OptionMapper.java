package sk.uteg.springdatatest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import sk.uteg.springdatatest.api.dto.OptionSummaryDTO;
import sk.uteg.springdatatest.db.model.Option;

@Mapper(componentModel = "spring")
public interface OptionMapper {
    OptionMapper INSTANCE = Mappers.getMapper(OptionMapper.class);

    @Mapping(target = "text", source = "option.text")
    OptionSummaryDTO toOptionSummaryDTO(Option option, int occurrences);
}
