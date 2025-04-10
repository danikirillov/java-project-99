package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskCreateRequest;
import hexlet.code.app.dto.TaskResponse;
import hexlet.code.app.dto.TaskUpdateRequest;
import hexlet.code.app.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
public abstract class TaskMapper {
    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "status", source = "taskStatus.name")
    @Mapping(target = "assigneeId", source = "assignee.id")
    public abstract TaskResponse toResponse(Task task);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    public abstract Task toEntity(TaskCreateRequest dto);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "index", source = "index")
    @Mapping(target = "assignee.id", source = "assigneeId")
    public abstract void updateEntity(@MappingTarget Task entity, TaskUpdateRequest dto);
} 