package uax.madm.devops.campaigns_demo.infrastructure.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import uax.madm.devops.campaigns_demo.application.services.TaskService;
import uax.madm.devops.campaigns_demo.domain.model.Task;
import uax.madm.devops.campaigns_demo.infrastructure.dto.TaskDto;
import uax.madm.devops.campaigns_demo.infrastructure.mapper.NewWorkerMapper;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/tasks")
public class TaskController {

    private TaskService taskService;
    private NewWorkerMapper newWorkerMapper;

    public TaskController(TaskService taskService, NewWorkerMapper newWorkerMapper) {
        this.taskService = taskService;
        this.newWorkerMapper = newWorkerMapper;
    }

    @GetMapping
    public List<TaskDto> searchTasks(@RequestParam(required = false) String name, @RequestParam(required = false) Long assigneeId) {
        List<Task> models = taskService.searchTasks(name, assigneeId);
        List<TaskDto> taskDtos = models.stream()
                .map(TaskDto.Mapper::toDto)
                .collect(Collectors.toList());
        return taskDtos;
    }

    @GetMapping(value = "/{id}")
    public TaskDto findOne(@PathVariable Long id) {
        Task model = taskService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return TaskDto.Mapper.toDto(model);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto create(@RequestBody @Valid TaskDto newTask) {
        Task model = TaskDto.Mapper.toModel(newTask);
        Task createdModel = this.taskService.save(model);
        return TaskDto.Mapper.toDto(createdModel);
    }

    @PutMapping(value = "/{id}")
    public TaskDto update(@PathVariable Long id, @RequestBody @Validated(TaskDto.TaskUpdateValidationData.class) TaskDto updatedTask) {
        Task model = TaskDto.Mapper.toModel(updatedTask);
        Task createdModel = this.taskService.updateTask(id, model)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return TaskDto.Mapper.toDto(createdModel);
    }

    @PutMapping(value = "/{id}/status")
    public TaskDto updateStatus(@PathVariable Long id, @RequestBody @Validated(TaskDto.TaskUpdateStatusValidationData.class) TaskDto taskWithStatus) {
        Task updatedModel = this.taskService.updateStatus(id, taskWithStatus.status())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return TaskDto.Mapper.toDto(updatedModel);
    }

    @PutMapping(value = "/{id}/assignee")
    public TaskDto updateAssignee(@PathVariable Long id, @RequestBody @Validated(TaskDto.TaskUpdateAssigneeValidationData.class) TaskDto taskWithAssignee) {
        Task updatedModel = this.taskService.updateAssignee(id, newWorkerMapper.toModel(taskWithAssignee.assignee()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return TaskDto.Mapper.toDto(updatedModel);
    }
}
