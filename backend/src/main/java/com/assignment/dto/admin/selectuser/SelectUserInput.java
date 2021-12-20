package com.assignment.dto.admin.selectuser;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SelectUserInput {
    private Integer userId;
    private String email;
    private String nickname;
    private String grade;
    private int page;
    private int size;
}
