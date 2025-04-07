package hexlet.code.app.mapper;

import hexlet.code.app.dto.UserCreateRequest;
import hexlet.code.app.dto.UserResponse;
import hexlet.code.app.dto.UserUpdateRequest;
import hexlet.code.app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {JsonNullableMapper.class}
)
public interface UserMapper {
    User toEntity(UserCreateRequest dto);

    User toEntity(UserResponse dto);

    UserResponse toResponse(User entity);

    void updateEntity(@MappingTarget User entity, UserUpdateRequest dto);
} 