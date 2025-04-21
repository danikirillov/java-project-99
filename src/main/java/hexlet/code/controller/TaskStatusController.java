package hexlet.code.controller;

import hexlet.code.dto.status.TaskStatusCreateRequest;
import hexlet.code.dto.status.TaskStatusResponse;
import hexlet.code.dto.status.TaskStatusUpdate;
import hexlet.code.service.TaskStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<List<TaskStatusResponse>> getAll(
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
    public TaskStatusResponse getTaskStatus(@PathVariable Long id) {
        return taskStatusService.getTaskStatusById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusResponse createTaskStatus(@Valid @RequestBody TaskStatusCreateRequest dto) {
        return taskStatusService.createTaskStatus(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskStatusResponse updateTaskStatus(@PathVariable Long id, @Valid @RequestBody TaskStatusUpdate dto) {
        return taskStatusService.updateTaskStatus(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTaskStatus(@PathVariable Long id) {
        taskStatusService.deleteTaskStatus(id);
    }
} 