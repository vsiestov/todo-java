package com.vsiestov.users.domain;

import com.vsiestov.shared.core.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class UserName {
    private String value;

    public static <T>Result create(String value, String validationMessage, String propertyName) {
        if (value == null || value.length() < 3) {
            return Result.fail(validationMessage, propertyName);
        }

        return Result.ok(new UserName(value));
    }
}
