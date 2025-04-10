package hexlet.code.app.controller;

import hexlet.code.app.dto.TaskStatusResponseDto;
import hexlet.code.app.service.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/task_statuses")
@RequiredArgsConstructor
public class TaskStatusController {
    private final TaskStatusService taskStatusService;

    @GetMapping
    public ResponseEntity<List<TaskStatusResponseDto>> getAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        var statuses = taskStatusService.getAllTaskStatuses();
        var skip = (page - 1) * limit;
        var statusesFiltered = statuses.stream().skip(skip).limit(limit).toList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(statuses.size()))
                .body(statusesFiltered);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskStatusResponseDto getTaskStatus(@PathVariable Long id) {
        return taskStatusService.getTaskStatusById(id);
    }
} 