package com.vsiestov.tasks.application;

import com.vsiestov.shared.core.Result;
import com.vsiestov.shared.core.ValidationDescription;
import com.vsiestov.shared.exceptions.ValidationException;
import com.vsiestov.tasks.domain.Task;
import com.vsiestov.tasks.domain.TaskDescription;
import com.vsiestov.tasks.domain.TaskName;
import com.vsiestov.tasks.mapper.TaskMapper;
import com.vsiestov.tasks.repository.TaskRepository;
import com.vsiestov.tasks.rest.dto.TaskDTO;
import com.vsiestov.tasks.rest.dto.UpdateTaskDTO;
import com.vsiestov.users.rest.dto.UserDTO;
import com.vsiestov.users.services.UsersService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class UpdateTaskUseCase {
    private final UsersService usersService;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public UpdateTaskUseCase(
        UsersService usersService,
        TaskRepository taskRepository,
        TaskMapper taskMapper
    ) {
        this.usersService = usersService;
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public TaskDTO execute(long id, UpdateTaskDTO updateTaskDTO) {
        Result<TaskName> taskNameResult = TaskName.create(updateTaskDTO.getName());
        Result<TaskDescription> taskDescriptionResult = TaskDescription.create(updateTaskDTO.getDescription());

        ArrayList<ValidationDescription> errors = Result.errors(new Result[]{
            taskNameResult,
            taskDescriptionResult
        });

        if (errors.size() > 0) {
            throw new ValidationException(errors);
        }

        UserDTO userDTO = usersService.getCurrentlyLoggedInUser();
        Optional<Task> taskOptional = taskRepository.findByIdAndUserId(id, userDTO.getId());

        if (!taskOptional.isPresent()) {
            ValidationException validationException = new ValidationException();
            validationException.addError(new ValidationDescription("taskId", "Task is not found for this user"));

            throw validationException;
        }

        Task task = taskOptional.get();

        task.setName(taskNameResult.getValue());
        task.setDescription(taskDescriptionResult.getValue());
        task.setComplete(updateTaskDTO.isComplete());

        return taskMapper.toDTO(taskRepository.save(task));
    }
}
