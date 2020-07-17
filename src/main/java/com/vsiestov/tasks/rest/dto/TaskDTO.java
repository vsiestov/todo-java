package com.vsiestov.tasks.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private long id;
    private String name;
    private String description;
    private boolean complete;
}
