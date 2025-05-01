package uax.madm.devops.campaigns_demo.application.services;

import java.util.List;
import java.util.Optional;

import uax.madm.devops.campaigns_demo.domain.model.Task;
import uax.madm.devops.campaigns_demo.domain.model.enums.TaskStatus;
import uax.madm.devops.campaigns_demo.domain.model.Worker;

public interface TaskService {

    List<Task> searchTasks(String nameSubstring, Long assigneeId);

    Optional<Task> findById(Long id);

    Task save(Task task);

    Optional<Task> updateTask(Long id, Task task);

    Optional<Task> updateStatus(Long id, TaskStatus status);

    Optional<Task> updateAssignee(Long id, Worker assignee);
}