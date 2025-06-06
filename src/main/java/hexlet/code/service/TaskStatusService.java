package hexlet.code.service;

import hexlet.code.dto.status.TaskStatusCreateRequest;
import hexlet.code.dto.status.TaskStatusResponse;
import hexlet.code.dto.status.TaskStatusUpdate;
import hexlet.code.exception.HasTasksException;
import hexlet.code.exception.TaskStatusNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskStatusService {
    private final TaskStatusRepository taskStatusRepository;
    private final TaskRepository taskRepository;
    private final TaskStatusMapper taskStatusMapper;

    public List<TaskStatusResponse> getAllTaskStatuses() {
        return taskStatusRepository.findAll()
            .stream()
            .map(taskStatusMapper::toDto)
            .toList();
    }

    public TaskStatusResponse getTaskStatusById(Long id) throws TaskStatusNotFoundException {
        return taskStatusRepository.findById(id)
            .map(taskStatusMapper::toDto)
            .orElseThrow(() -> new TaskStatusNotFoundException(id));
    }

    public TaskStatusResponse createTaskStatus(TaskStatusCreateRequest dto) {
        var taskStatus = taskStatusMapper.toEntity(dto);
        taskStatus = taskStatusRepository.save(taskStatus);
        return taskStatusMapper.toDto(taskStatus);
    }

    public TaskStatusResponse updateTaskStatus(Long id, TaskStatusUpdate dto) throws TaskStatusNotFoundException {
        var taskStatus = taskStatusRepository.findById(id)
            .orElseThrow(() -> new TaskStatusNotFoundException(id));

        taskStatusMapper.updateEntity(taskStatus, dto);
        var updatedTaskStatus = taskStatusRepository.save(taskStatus);
        return taskStatusMapper.toDto(updatedTaskStatus);
    }

    @Transactional
    public void deleteTaskStatus(Long id) {
        var taskStatus = taskStatusRepository.findById(id)
            .orElseThrow(() -> new TaskStatusNotFoundException(id));

        if (taskRepository.existsByTaskStatus(taskStatus)) {
            throw new HasTasksException(id);
        }

        taskStatusRepository.delete(taskStatus);
    }
} 