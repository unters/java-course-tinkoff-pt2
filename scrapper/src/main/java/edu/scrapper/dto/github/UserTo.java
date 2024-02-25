package edu.scrapper.dto.github;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class UserTo {

    private final Integer id;
    private final String login;

    public UserTo(
        Integer id,
        String login
    ) {
        this.id = id;
        this.login = login;
    }
}
