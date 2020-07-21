package com.vsiestov.tasks.rest.dto;

import lombok.Builder;

@Builder
public class TaskDTO {
    private long id;
    private String name;
    private String description;
    private boolean complete;
}
