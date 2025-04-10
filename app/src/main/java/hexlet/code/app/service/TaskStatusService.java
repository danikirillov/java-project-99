package hexlet.code.app.service;

import hexlet.code.app.dto.TaskStatusResponseDto;
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
} 