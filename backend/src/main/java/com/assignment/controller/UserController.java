package com.assignment.controller;

import com.assignment.dto.user.email.EmailInput;
import com.assignment.dto.user.email.EmailOutput;
import com.assignment.dto.user.jwt.JwtOutput;
import com.assignment.dto.user.reissue.ReissueOutput;
import com.assignment.dto.user.selectprofile.SelectProfileOutput;
import com.assignment.dto.user.signin.SignInInput;
import com.assignment.dto.user.signin.SignInOutput;
import com.assignment.dto.user.signup.SignUpOutput;
import com.assignment.dto.user.signup.SignUpInput;
import com.assignment.dto.user.updateprofile.UpdateProfileInput;
import com.assignment.response.Response;
import com.assignment.service.JwtService;
import com.assignment.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.assignment.response.ResponseStatus.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    /**
     * 회원가입 API [POST] /api/users/signup
     * 
     * @return ResponseEntity<Response<SignUpOutput>>
     */
    // Body
    @PostMapping("/signup")
    public ResponseEntity<Response<SignUpOutput>> signUp(@RequestBody SignUpInput signUpInput) {
        log.info("[POST] /api/users/signup");
        return userService.signUp(signUpInput);
    }

    /**
     * 로그인 API [POST] /api/users/signin
     * 
     * @return ResponseEntity<<Response<SignInOutput>>
     */
    // Body
    @PostMapping("/signin")
    public ResponseEntity<Response<SignInOutput>> signIn(@RequestBody SignInInput signInInput) {
        log.info("[POST] /api/users/signin");
        return userService.signIn(signInInput);
    }

    /**
     * 회원탈퇴 API
     * [DELETE] /api/users
     * @return ResponseEntity<<Response<Object>>
     */
    @DeleteMapping()
    public ResponseEntity<Response<Object>> resign () {
        log.info("[DELETE] /api/users");
        return userService.resign();
    }

    @PostMapping("/jwt")
    public ResponseEntity<Response<JwtOutput>> jwt() {
        System.out.println("[POST] /api/users/jwt");
        int userId = jwtService.getUserId();
        if (userId == -1)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Response<>(UNAUTHORIZED_TOKEN));
        if (userId == -2)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(BAD_ACCESS_TOKEN_VALUE));
        if (userId == -3)
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new Response<>(FORBIDDEN_USER_ID));
        JwtOutput jwtOutput = new JwtOutput(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Response<>(jwtOutput, SUCCESS_SIGN_IN));
    }

    /**
     * ACESS TOKEN 재발급 API [POST] /api/users/reissue
     *
     * @return ResponseEntity<Response<ReissueOutput>>
     */
    // Body
    @GetMapping("/reissue")
    public ResponseEntity<Response<ReissueOutput>> reissueAccessToken() {
        log.info("[POST] /api/users/reissue");
        String accessToken = jwtService.refreshAccessToken();
        if(accessToken==null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Response<>(UNAUTHORIZED_REFRESH_TOKEN));
        ReissueOutput result = ReissueOutput.builder()
                .accessToken(accessToken)
                .userId(jwtService.getUserId(accessToken)).build();
        return ResponseEntity.status(HttpStatus.OK)
                    .body(new Response<>(result, SUCCESS_TO_REISSUE_ACCESS_TOKEN));
    }


    /**
     * 이메일 인증 API [POST] /api/users/email
     * 
     * @return ResponseEntity<Response<EmailOutput>>
     */
    // Body
    @PostMapping("/email")
    public ResponseEntity<Response<EmailOutput>> mailSend(@RequestBody EmailInput emailInput) {
        log.info("[POST] /users/email");
        return userService.sendMail(emailInput);
    }

    /**
     * 프로필 조회 API [GET] /api/users/me
     *
     * @return ResponseEntity<Response<EmailOutput>>
     */
    // Body
    @GetMapping("/me")
    public ResponseEntity<Response<SelectProfileOutput>> selectProfile() {
        log.info("[GET] /users/me");
        return userService.selectProfile();
    }

    /**
     * 프로필 수정 API [PATCH] /api/users/
     *
     * @return ResponseEntity<Response<Object>>
     */
    // Body
    @PatchMapping
    public ResponseEntity<Response<Object>> updateProfile(@RequestBody UpdateProfileInput updateProfileInput) {
        log.info("[PATCH] /users/");
        return userService.updateProfile(updateProfileInput);
    }
}
