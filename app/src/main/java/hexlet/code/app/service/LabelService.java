package hexlet.code.app.service;

import hexlet.code.app.dto.LabelCreateRequest;
import hexlet.code.app.dto.LabelResponse;
import hexlet.code.app.dto.LabelUpdateRequest;
import hexlet.code.app.exception.LabelNotFoundException;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    public LabelResponse getLabelById(Long id) {
        var label = labelRepository.findById(id)
            .orElseThrow(() -> new LabelNotFoundException(id));
        return labelMapper.toResponse(label);
    }

    public List<LabelResponse> getAllLabels() {
        return labelRepository.findAll().stream()
            .map(labelMapper::toResponse)
            .toList();
    }

    public LabelResponse createLabel(LabelCreateRequest request) {
        var label = labelMapper.toEntity(request);
        var savedLabel = labelRepository.save(label);
        return labelMapper.toResponse(savedLabel);
    }

    public LabelResponse updateLabel(Long id, LabelUpdateRequest request) {
        var label = labelRepository.findById(id)
            .orElseThrow(() -> new LabelNotFoundException(id));
        labelMapper.updateEntity(label, request);
        var updatedLabel = labelRepository.save(label);
        return labelMapper.toResponse(updatedLabel);
    }

    public void deleteLabel(Long id) {
        if (!labelRepository.existsById(id)) {
            throw new LabelNotFoundException(id);
        }
        labelRepository.deleteById(id);
    }
} 