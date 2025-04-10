package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskStatusCreateDto;
import hexlet.code.app.dto.TaskStatusResponseDto;
import hexlet.code.app.dto.TaskStatusUpdateDto;
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
    public abstract TaskStatusResponseDto toDto(TaskStatus taskStatus);

    public abstract TaskStatus toEntity(TaskStatusCreateDto dto);

    public abstract void updateEntity(@MappingTarget TaskStatus taskStatus, TaskStatusUpdateDto dto);
} 