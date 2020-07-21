package com.vsiestov.tasks.application;

import com.vsiestov.shared.core.Result;
import com.vsiestov.shared.core.ValidationDescription;
import com.vsiestov.shared.exceptions.ValidationException;
import com.vsiestov.tasks.domain.Task;
import com.vsiestov.tasks.domain.TaskDescription;
import com.vsiestov.tasks.domain.TaskName;
import com.vsiestov.tasks.mapper.TaskMapper;
import com.vsiestov.tasks.repository.TaskRepository;
import com.vsiestov.tasks.rest.dto.CreateTaskDTO;
import com.vsiestov.tasks.rest.dto.TaskDTO;
import com.vsiestov.users.rest.dto.UserDTO;
import com.vsiestov.users.services.UsersService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CreateTaskUseCase {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UsersService usersService;

    public CreateTaskUseCase(
        TaskRepository taskRepository,
        TaskMapper taskMapper,
        UsersService usersService
    ) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.usersService = usersService;
    }

    public TaskDTO execute(CreateTaskDTO request) {
        Result<TaskName> taskNameResult = TaskName.create(request.getName());
        Result<TaskDescription> taskDescriptionResult = TaskDescription.create(request.getDescription());

        ArrayList<ValidationDescription> errors = Result.errors(new Result[]{
            taskNameResult,
            taskDescriptionResult
        });

        if (errors.size() > 0) {
            throw new ValidationException(errors);
        }

        UserDTO user = usersService.getCurrentlyLoggedInUser();

        Task task = Task.builder()
            .name(taskNameResult.getValue())
            .description(taskDescriptionResult.getValue())
            .userId(user.getId())
            .complete(false)
            .build();

        return taskMapper.toDTO(taskRepository.save(task));
    }
}
