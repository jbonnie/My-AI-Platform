package myAI.portal.adapter.in.dto;

import lombok.Getter;
import myAI.portal.application.domain.entity.User;

@Getter
public class SignupRequestDto {
    private String username;
    private String password;
    private String apiKey;

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .apiKey(apiKey)
                .build();
    }
}
