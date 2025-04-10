package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskResponse {
    private Long id;
    private Integer index;
    private LocalDate createdAt;
    private Long assigneeId;
    private String title;
    private String content;
    private String status;
} 