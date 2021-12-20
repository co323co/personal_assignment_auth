package com.assignment.dto.admin.selectuser;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@Builder
@Getter
@Setter
public class SelectUserOutput {
    private int userId;
    private String userEmail;
    private String userNickname;
    private String userGrade;
    private Date userCreatedAt;
    private Date userUpdatedAt;

    @QueryProjection
    public SelectUserOutput(int userId, String userEmail, String userNickname, String userGrade, Date userCreatedAt, Date userUpdatedAt) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userNickname = userNickname;
        this.userGrade = userGrade;
        this.userCreatedAt = userCreatedAt;
        this.userUpdatedAt = userUpdatedAt;
    }
}
