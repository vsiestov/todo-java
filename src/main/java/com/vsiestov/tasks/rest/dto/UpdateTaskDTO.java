package com.vsiestov.tasks.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskDTO {
    @NotNull
    @Size(min = 3, max = 255)
    private String name;

    @Size(max = 255)
    private String description;

    private boolean complete;
}
