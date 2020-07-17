package com.vsiestov.users.domain;

import com.vsiestov.shared.core.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;

import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class UserEmail {
    private static final EmailValidator validator = EmailValidator.getInstance();
    private String value;

    public static <T>Result create(String email) {
        if (!validator.isValid(email)) {
            return Result.fail("Provided email is not valid", "email");
        }

        return Result.ok(new UserEmail(email));
    }
}
