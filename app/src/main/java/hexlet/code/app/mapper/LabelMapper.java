package hexlet.code.app.mapper;

import hexlet.code.app.dto.LabelCreateRequest;
import hexlet.code.app.dto.LabelResponse;
import hexlet.code.app.dto.LabelUpdateRequest;
import hexlet.code.app.model.Label;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {JsonNullableMapper.class}
)
public interface LabelMapper {
    LabelResponse toResponse(Label entity);

    Label toEntity(LabelCreateRequest dto);

    void updateEntity(@MappingTarget Label entity, LabelUpdateRequest dto);
} 