package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskStatusCreateRequest;
import hexlet.code.app.dto.TaskStatusResponse;
import hexlet.code.app.dto.TaskStatusUpdate;
import hexlet.code.app.model.TaskStatus;
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
public abstract class TaskStatusMapper {
    public abstract TaskStatusResponse toDto(TaskStatus taskStatus);

    public abstract TaskStatus toEntity(TaskStatusCreateRequest dto);

    public abstract TaskStatus toEntity(TaskStatusResponse dto);

    public abstract void updateEntity(@MappingTarget TaskStatus taskStatus, TaskStatusUpdate dto);
} 