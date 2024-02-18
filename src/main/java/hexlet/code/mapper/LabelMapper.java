package hexlet.code.mapper;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", config = MapperConfig.class)
public interface LabelMapper {

    Label toLabel(LabelDto dto);
    void updateLabel(@MappingTarget Label label, LabelDto dto);
}
