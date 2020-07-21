package com.vsiestov.tasks.mapper;

import com.vsiestov.tasks.domain.Task;
import com.vsiestov.tasks.rest.dto.TaskDTO;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public TaskDTO toDTO(Task task) {
        return TaskDTO.builder()
            .id(task.getId())
            .name(task.getName().getValue())
            .description(task.getDescription().getValue())
            .complete(task.isComplete())
            .build();
    }
}
