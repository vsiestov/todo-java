package com.vsiestov.users.domain;

import com.vsiestov.shared.core.Result;
import com.vsiestov.shared.regexp.RegExpResources;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPassword {
    private static Pattern pattern = Pattern.compile(RegExpResources.PASSWORD_PATTERN);
    private String value;

    public static <T>Result create(String value) {
        Matcher matcher = pattern.matcher(value);

        if (!matcher.matches()) {
            return Result.fail("Your password is not valid", "password");
        }

        return Result.ok(new UserPassword(value));
    }
}
