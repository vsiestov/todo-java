package com.vsiestov.tasks.application;

import com.vsiestov.platform.RestConstants;
import com.vsiestov.tasks.mapper.TaskMapper;
import com.vsiestov.tasks.repository.TaskRepository;
import com.vsiestov.tasks.rest.dto.TaskDTO;
import com.vsiestov.users.rest.dto.UserDTO;
import com.vsiestov.users.services.UsersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class RetrieveTasksUseCase {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UsersService usersService;

    public RetrieveTasksUseCase(
        TaskRepository taskRepository,
        TaskMapper taskMapper,
        UsersService usersService
    ) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.usersService = usersService;
    }

    public Page<TaskDTO> execute(int page, int size) {
        if (size <= 0) {
            size = RestConstants.DEFAULT_SIZE;
        }

        UserDTO userDTO = usersService.getCurrentlyLoggedInUser();

        return taskRepository.findAllByUserIdEquals(userDTO.getId(), PageRequest.of(page, size, Sort.by("id")))
            .map(taskMapper::toDTO);
    }
}
