package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskCreateRequest;
import hexlet.code.app.dto.TaskResponse;
import hexlet.code.app.dto.TaskUpdateRequest;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

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
    @Mapping(target = "taskLabelIds", source = "labels", qualifiedByName = "mapLabelsToIds")
    public abstract TaskResponse toResponse(Task task);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "mapIdsToLabels")
    public abstract Task toEntity(TaskCreateRequest dto);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "index", source = "index")
    @Mapping(target = "assignee.id", source = "assigneeId")
    public abstract void updateEntity(@MappingTarget Task entity, TaskUpdateRequest dto);

    @Named("mapLabelsToIds")
    protected Set<Long> mapLabelsToIds(Set<Label> labels) {
        if (labels == null) {
            return Set.of();
        }
        return labels.stream()
            .map(Label::getId)
            .collect(Collectors.toSet());
    }

    @Named("mapIdsToLabels")
    protected Set<Label> mapIdsToLabels(Set<Long> labelIds) {
        if (labelIds == null) {
            return Set.of();
        }
        return labelIds.stream()
            .map(id -> {
                var label = new Label();
                label.setId(id);
                return label;
            })
            .collect(Collectors.toSet());
    }
} 