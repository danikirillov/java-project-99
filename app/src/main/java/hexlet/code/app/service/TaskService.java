package hexlet.code.app.service;

import hexlet.code.app.dto.TaskCreateRequest;
import hexlet.code.app.dto.TaskResponse;
import hexlet.code.app.dto.TaskUpdateRequest;
import hexlet.code.app.exception.TaskNotFoundException;
import hexlet.code.app.exception.TaskStatusNotFoundException;
import hexlet.code.app.mapper.JsonNullableMapper;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;
    private final TaskMapper taskMapper;
    private final JsonNullableMapper jsonNullableMapper;

    public TaskResponse getTaskById(Long id) {
        var task = taskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));
        return taskMapper.toResponse(task);
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
            .map(taskMapper::toResponse)
            .toList();
    }

    public TaskResponse createTask(TaskCreateRequest request) {
        var task = taskMapper.toEntity(request);

        var status = taskStatusRepository.findBySlug(request.getStatus())
            .orElseThrow(() -> new TaskStatusNotFoundException(request.getStatus()));
        task.setTaskStatus(status);

        if (request.getAssigneeId() != null) {
            var assignee = userRepository.findById(request.getAssigneeId()).get();
            task.setAssignee(assignee);
        }

        if (request.getTaskLabelIds() != null) {
            var labels = request.getTaskLabelIds().stream()
                .map(labelRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            task.setLabels(labels);
        }

        var savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        var task = taskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));
        taskMapper.updateEntity(task, request);
        var nullableStatus = request.getStatus();
        if (jsonNullableMapper.isPresent(nullableStatus)) {
            var status = taskStatusRepository.findByName(nullableStatus.get())
                .orElseThrow(() -> new TaskStatusNotFoundException(nullableStatus.get()));
            task.setTaskStatus(status);
        }

        var nullableLabelIds = request.getTaskLabelIds();
        if (jsonNullableMapper.isPresent(nullableLabelIds)) {
            var labels = nullableLabelIds.get().stream()
                .map(labelRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            task.setLabels(labels);
        }

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