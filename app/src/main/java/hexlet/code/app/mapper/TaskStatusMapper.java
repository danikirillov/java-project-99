package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskStatusResponseDto;
import hexlet.code.app.model.TaskStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskStatusMapper {
    TaskStatusResponseDto toDto(TaskStatus taskStatus);
} 