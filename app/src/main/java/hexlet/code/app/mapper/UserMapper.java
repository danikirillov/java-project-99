package hexlet.code.app.mapper;

import hexlet.code.app.dto.user.UserCreateRequest;
import hexlet.code.app.dto.user.UserResponse;
import hexlet.code.app.dto.user.UserUpdateRequest;
import hexlet.code.app.model.User;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {JsonNullableMapper.class}
)
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JsonNullableMapper jsonNullableMapper;

    public abstract User toEntity(UserCreateRequest dto);

    public abstract User toEntity(UserResponse dto);

    public abstract UserResponse toResponse(User entity);

    public abstract void updateEntity(@MappingTarget User entity, UserUpdateRequest dto);

    @BeforeMapping
    public void encryptPassword(UserCreateRequest request) {
        var password = request.getPassword();
        request.setPassword(passwordEncoder.encode(password));
    }

    @BeforeMapping
    public void encryptPassword(UserUpdateRequest request) {
        var password = request.getPassword();
        if (jsonNullableMapper.isPresent(password)) {
            var encodedPassword = JsonNullable.of(passwordEncoder.encode(password.get()));
            request.setPassword(encodedPassword);
        }
    }
} 