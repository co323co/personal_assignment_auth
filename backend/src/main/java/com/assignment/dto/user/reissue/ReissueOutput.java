package com.assignment.dto.user.reissue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ReissueOutput {
    private int userId;
    private String accessToken;
}