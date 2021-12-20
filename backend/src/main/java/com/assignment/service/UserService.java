package com.assignment.service;

import com.assignment.dto.admin.deleteuser.DeleteUserInput;
import com.assignment.dto.admin.selectuser.SelectUserInput;
import com.assignment.dto.admin.selectuser.SelectUserOutput;
import com.assignment.dto.user.email.EmailInput;
import com.assignment.dto.user.email.EmailOutput;
import com.assignment.dto.user.selectprofile.SelectProfileOutput;
import com.assignment.dto.user.signin.SignInInput;
import com.assignment.dto.user.updateprofile.UpdateProfileInput;
import com.assignment.response.PageResponse;
import com.assignment.response.Response;
import com.assignment.dto.user.signin.SignInOutput;
import com.assignment.dto.user.signup.SignUpInput;
import com.assignment.dto.user.signup.SignUpOutput;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<Response<SignInOutput>> signIn(SignInInput signInInput);
    ResponseEntity<Response<SignUpOutput>> signUp(SignUpInput signUpInput);
    ResponseEntity<Response<EmailOutput>> sendMail(EmailInput emailInput);
    ResponseEntity<Response<Object>> resign();
    ResponseEntity<Response<SelectProfileOutput>> selectProfile();
    ResponseEntity<Response<Object>> updateProfile(UpdateProfileInput updateProfileInput);
    ResponseEntity<PageResponse<SelectUserOutput>> selectUserList(SelectUserInput selectUserInput);
    ResponseEntity<Response<Object>> deleteUserList(List<DeleteUserInput> userIdList);
}
