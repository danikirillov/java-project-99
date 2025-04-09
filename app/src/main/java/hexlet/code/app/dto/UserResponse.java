package hexlet.code.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate createdAt;
} 