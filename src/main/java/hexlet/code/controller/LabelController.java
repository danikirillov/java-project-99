package hexlet.code.controller;

import hexlet.code.dto.label.LabelCreateRequest;
import hexlet.code.dto.label.LabelResponse;
import hexlet.code.dto.label.LabelUpdateRequest;
import hexlet.code.service.LabelService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
@RequiredArgsConstructor
public class LabelController {
    private final LabelService labelService;

    @GetMapping
    public ResponseEntity<List<LabelResponse>> getAllLabels() {
        var labels = labelService.getAllLabels();
        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(labels.size()))
            .body(labels);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelResponse getLabelById(@PathVariable Long id) {
        return labelService.getLabelById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LabelResponse createLabel(@Valid @RequestBody LabelCreateRequest request) {
        return labelService.createLabel(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelResponse updateLabel(@PathVariable Long id, @Valid @RequestBody LabelUpdateRequest request) {
        return labelService.updateLabel(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLabel(@PathVariable Long id) {
        labelService.deleteLabel(id);
    }
} 