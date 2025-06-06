package hexlet.code.service;

import hexlet.code.dto.user.UserCreateRequest;
import hexlet.code.dto.user.UserResponse;
import hexlet.code.dto.user.UserUpdateRequest;
import hexlet.code.exception.HasTasksException;
import hexlet.code.exception.UserNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final UserMapper userMapper;

    public UserResponse getUserById(Long id) {
        var user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
            .map(userMapper::toResponse)
            .toList();
    }

    public UserResponse createUser(UserCreateRequest request) {
        var user = userMapper.toEntity(request);
        var savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        var user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));

        userMapper.updateEntity(user, request);
        var updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        var user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));

        if (taskRepository.existsByAssignee(user)) {
            throw new HasTasksException(id);
        }

        userRepository.delete(user);
    }
} 