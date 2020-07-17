package com.vsiestov.tasks.mapper;

import com.vsiestov.tasks.domain.Task;
import com.vsiestov.tasks.rest.dto.TaskDTO;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public TaskDTO toDTO(Task task) {
        return new TaskDTO(
            task.getId(),
            task.getName().getValue(),
            task.getDescription().getValue(),
            task.isComplete()
        );
    }
}
