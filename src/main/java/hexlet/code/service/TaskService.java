package hexlet.code.service;

import hexlet.code.dto.task.TaskCreateRequest;
import hexlet.code.dto.task.TaskFilterProperties;
import hexlet.code.dto.task.TaskResponse;
import hexlet.code.dto.task.TaskUpdateRequest;
import hexlet.code.exception.TaskNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskResponse getTaskById(Long id) {
        var task = taskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));
        return taskMapper.toResponse(task);
    }

    public List<TaskResponse> getFilteredTasks(TaskFilterProperties taskFilterProperties) {
        var spec = Specification.<Task>where(null)
            .and(TaskSpecification.withTitleContaining(taskFilterProperties.getTitleCont()))
            .and(TaskSpecification.withAssigneeId(taskFilterProperties.getAssigneeId()))
            .and(TaskSpecification.withStatus(taskFilterProperties.getStatus()))
            .and(TaskSpecification.withLabelId(taskFilterProperties.getLabelId()));

        return taskRepository.findAll(spec).stream()
            .map(taskMapper::toResponse)
            .toList();
    }

    public TaskResponse createTask(TaskCreateRequest request) {
        var task = taskMapper.toEntity(request);
        var savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        var task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        taskMapper.updateEntity(task, request);
        var updatedTask = taskRepository.save(task);
        return taskMapper.toResponse(updatedTask);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }
} 