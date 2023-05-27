package com.publicapi.test.domain.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
@Builder
public class UserInfo {

    @Email(message = "이메일 형식이 맞지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String username;

    private String profileImage;

    @Builder
    public UserInfo(String email, String name, String username, String profileImage) {
        this.email = email;
        this.name = name;
        this.username = username;
        this.profileImage = profileImage;
    }
}
