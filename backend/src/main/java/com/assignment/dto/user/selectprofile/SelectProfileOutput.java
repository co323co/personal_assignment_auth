package com.assignment.dto.user.selectprofile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class SelectProfileOutput {
    private int userId;
    private String userEmail;
    private String userNickname;
    private String userGrade;
    private Date userCreatedAt;
    private Date userUpdatedAt;
}
