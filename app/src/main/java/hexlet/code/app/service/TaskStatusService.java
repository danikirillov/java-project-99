package hexlet.code.app.service;

import hexlet.code.app.dto.TaskStatusCreateDto;
import hexlet.code.app.dto.TaskStatusResponseDto;
import hexlet.code.app.dto.TaskStatusUpdateDto;
import hexlet.code.app.exception.TaskStatusNotFoundException;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.repository.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskStatusService {
    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusMapper taskStatusMapper;

    public List<TaskStatusResponseDto> getAllTaskStatuses() {
        return taskStatusRepository.findAll()
            .stream()
            .map(taskStatusMapper::toDto)
            .toList();
    }

    public TaskStatusResponseDto getTaskStatusById(Long id) throws TaskStatusNotFoundException {
        return taskStatusRepository.findById(id)
            .map(taskStatusMapper::toDto)
            .orElseThrow(() -> new TaskStatusNotFoundException(id));
    }

    public TaskStatusResponseDto createTaskStatus(TaskStatusCreateDto dto) {
        var taskStatus = taskStatusMapper.toEntity(dto);
        taskStatus = taskStatusRepository.save(taskStatus);
        return taskStatusMapper.toDto(taskStatus);
    }

    public TaskStatusResponseDto updateTaskStatus(Long id, TaskStatusUpdateDto dto) throws TaskStatusNotFoundException {
        var taskStatus = taskStatusRepository.findById(id)
            .orElseThrow(() -> new TaskStatusNotFoundException(id));

        taskStatusMapper.updateEntity(taskStatus, dto);
        var updatedTaskStatus = taskStatusRepository.save(taskStatus);
        return taskStatusMapper.toDto(updatedTaskStatus);
    }

    public void deleteTaskStatus(Long id) {
        if (!taskStatusRepository.existsById(id)) {
            throw new TaskStatusNotFoundException(id);
        }
        taskStatusRepository.deleteById(id);
    }
} 