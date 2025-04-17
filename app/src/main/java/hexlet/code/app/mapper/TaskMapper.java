package hexlet.code.app.mapper;

import hexlet.code.app.dto.task.TaskCreateRequest;
import hexlet.code.app.dto.task.TaskResponse;
import hexlet.code.app.dto.task.TaskUpdateRequest;
import hexlet.code.app.exception.LabelNotFoundException;
import hexlet.code.app.exception.TaskStatusNotFoundException;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.CollectionMappingStrategy.TARGET_IMMUTABLE;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    collectionMappingStrategy = TARGET_IMMUTABLE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {JsonNullableMapper.class, ReferenceMapper.class}
)
@Slf4j
public abstract class TaskMapper {
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    public Set<Label> toLabels(Set<Long> taskLabelIds) {
        if (taskLabelIds == null) {
            return null;
        }

        return taskLabelIds.stream()
            .map(id -> labelRepository.findById(id).orElseThrow(() -> new LabelNotFoundException(id)))
            .collect(Collectors.toSet());
    }

    public Set<Long> toTaskLabelIds(Set<Label> labels) {
        return labels.stream().map(Label::getId).collect(Collectors.toSet());
    }

    public TaskStatus toTaskStatus(String statusSlag) {
        return taskStatusRepository.findBySlug(statusSlag)
            .orElseThrow(() -> new TaskStatusNotFoundException(statusSlag));
    }

    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "taskLabelIds", source = "labels")
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "status", source = "taskStatus.slug")
    public abstract TaskResponse toResponse(Task task);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "labels", source = "taskLabelIds")
    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "taskStatus", source = "status")
    public abstract Task toEntity(TaskCreateRequest dto);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "labels", source = "taskLabelIds")
    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "taskStatus", source = "status")
    public abstract void updateEntity(@MappingTarget Task entity, TaskUpdateRequest dto);
}
