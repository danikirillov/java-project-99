package hexlet.code.mapper;

import hexlet.code.dto.label.LabelCreateRequest;
import hexlet.code.dto.label.LabelResponse;
import hexlet.code.dto.label.LabelUpdateRequest;
import hexlet.code.model.Label;
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
public abstract class LabelMapper {
    public abstract LabelResponse toResponse(Label entity);

    public abstract Label toEntity(LabelCreateRequest dto);

    public abstract void updateEntity(@MappingTarget Label entity, LabelUpdateRequest dto);
} 