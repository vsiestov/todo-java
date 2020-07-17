package com.vsiestov.tasks.application;

import com.vsiestov.shared.core.ValidationDescription;
import com.vsiestov.shared.exceptions.ValidationException;
import com.vsiestov.tasks.domain.Task;
import com.vsiestov.tasks.mapper.TaskMapper;
import com.vsiestov.tasks.repository.TaskRepository;
import com.vsiestov.tasks.rest.dto.TaskDTO;
import com.vsiestov.users.rest.dto.UserDTO;
import com.vsiestov.users.services.UsersService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RetrieveTaskByIdUseCase {
    private final TaskRepository taskRepository;
    private final UsersService usersService;
    private final TaskMapper taskMapper;

    public RetrieveTaskByIdUseCase(
        TaskRepository taskRepository,
        UsersService usersService,
        TaskMapper taskMapper
    ) {
        this.taskRepository = taskRepository;
        this.usersService = usersService;
        this.taskMapper = taskMapper;
    }

    public TaskDTO execute(long taskId) {
        UserDTO userDTO = usersService.getCurrentlyLoggedInUser();

        Optional<Task> task = taskRepository.findByIdAndUserId(taskId, userDTO.getId());

        if (task.isPresent()) {
            return taskMapper.toDTO(task.get());
        }

        ValidationException validationException = new ValidationException();
        validationException.addError(new ValidationDescription(
            "taskId",
            "Task with provided id is not found for this user"
        ));

        throw validationException;
    }
}
