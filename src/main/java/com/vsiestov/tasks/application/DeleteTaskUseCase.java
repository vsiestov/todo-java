package com.vsiestov.tasks.application;

import com.vsiestov.tasks.repository.TaskRepository;
import com.vsiestov.tasks.rest.dto.DeleteTaskDTO;
import com.vsiestov.users.rest.dto.UserDTO;
import com.vsiestov.users.services.UsersService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DeleteTaskUseCase {
    private final TaskRepository taskRepository;
    private final UsersService usersService;

    public DeleteTaskUseCase(
        TaskRepository taskRepository,
        UsersService usersService
    ) {
        this.taskRepository = taskRepository;
        this.usersService = usersService;
    }

    @Transactional
    public DeleteTaskDTO execute(long taskId) {
        UserDTO userDTO = usersService.getCurrentlyLoggedInUser();

        taskRepository.deleteTaskByIdAndUserId(taskId, userDTO.getId());

        return new DeleteTaskDTO("Your task has been deleted");
    }
}
