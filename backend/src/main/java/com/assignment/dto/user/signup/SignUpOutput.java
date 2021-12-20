package com.assignment.dto.user.signup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class SignUpOutput {
    private int userId;
    private String accessToken;
    private String refreshToken;
}
