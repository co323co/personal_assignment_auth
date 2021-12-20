package com.assignment.dto.user.signup;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter

public class SignUpInput {
    private String email;
    private String password;
    private String nickname;
    private String grade;
}
