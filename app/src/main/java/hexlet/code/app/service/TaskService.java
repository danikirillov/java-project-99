package hexlet.code.app.service;

import hexlet.code.app.dto.task.TaskCreateRequest;
import hexlet.code.app.dto.task.TaskResponse;
import hexlet.code.app.dto.task.TaskUpdateRequest;
import hexlet.code.app.exception.TaskNotFoundException;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskSpecification;
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

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
            .map(taskMapper::toResponse)
            .toList();
    }

    public List<TaskResponse> getFilteredTasks(String titleCont, Long assigneeId, String status, Long labelId) {
        Specification<Task> spec = Specification.<Task>where(null)
            .and(TaskSpecification.withTitleContaining(titleCont))
            .and(TaskSpecification.withAssigneeId(assigneeId))
            .and(TaskSpecification.withStatus(status))
            .and(TaskSpecification.withLabelId(labelId));

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