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
public class TaskDescription {
    private String value;

    public static <T> Result create(String value) {
        if (value == null || value.length() > 255) {
            return Result.fail("Task description is too long", "description");
        }

        return Result.ok(new TaskDescription(value));
    }
}
