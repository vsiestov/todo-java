package com.vsiestov.tasks.domain;

import com.vsiestov.shared.core.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskName {
    private String value;

    public static <T>Result create(String value) {
        if (value == null || value.length() < 3 || value.length() > 255) {
            return Result.fail("Task name is not valid", "name");
        }

        return Result.ok(new TaskName(value));
    }
}
