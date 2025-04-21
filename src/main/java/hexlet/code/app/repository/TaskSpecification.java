package hexlet.code.app.repository;

import hexlet.code.app.model.Task;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {
    public static Specification<Task> withTitleContaining(String titleCont) {
        return (root, query, cb) -> titleCont == null ? cb.conjunction() :
            cb.like(cb.lower(root.get("name")), "%" + titleCont.toLowerCase() + "%");
    }

    public static Specification<Task> withAssigneeId(Long assigneeId) {
        return (root, query, cb) -> assigneeId == null ? cb.conjunction() :
            cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    public static Specification<Task> withStatus(String status) {
        return (root, query, cb) -> status == null ? cb.conjunction() :
            cb.equal(root.get("taskStatus").get("slug"), status);
    }

    public static Specification<Task> withLabelId(Long labelId) {
        return (root, query, cb) -> labelId == null ? cb.conjunction() :
            cb.equal(root.join("labels").get("id"), labelId);
    }
} 