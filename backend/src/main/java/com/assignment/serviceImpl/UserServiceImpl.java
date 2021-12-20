package com.assignment.serviceImpl;

import com.assignment.configuration.AES128;
import com.assignment.configuration.ValidationCheck;
import com.assignment.dto.admin.deleteuser.DeleteUserInput;
import com.assignment.dto.admin.selectuser.SelectUserInput;
import com.assignment.dto.admin.selectuser.SelectUserOutput;
import com.assignment.dto.user.email.EmailInput;
import com.assignment.dto.user.email.EmailOutput;
import com.assignment.dto.user.selectprofile.SelectProfileOutput;
import com.assignment.dto.user.signin.SignInInput;
import com.assignment.dto.user.signup.SignUpInput;
import com.assignment.dto.user.updateprofile.UpdateProfileInput;
import com.assignment.entity.User;
import com.assignment.response.PageResponse;
import com.assignment.response.Response;
import com.assignment.service.JwtService;
import com.assignment.service.UserService;
import com.assignment.dao.UserRepository;
import com.assignment.dto.user.signin.SignInOutput;
import com.assignment.dto.user.signup.SignUpOutput;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.RandomStringUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

import static com.assignment.response.ResponseStatus.*;

@Service("UserService")
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final JavaMailSender mailSender;

    @Value("${custom.constant.user.info.password.key}")
    private String USER_INFO_PASSWORD_KEY;

    @Override
    public ResponseEntity<Response<SignInOutput>> signIn(SignInInput signInInput) {
        // 1. 값 형식 체크
        if (signInInput == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(NO_VALUES));
        if (!ValidationCheck.isValid(signInInput.getEmail()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(BAD_EMAIL_VALUE));
        if (!ValidationCheck.isValid(signInInput.getPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(BAD_PASSWORD_VALUE));

        // 2. user 정보 가져오기
        User userDB;
        try {
            String email = signInInput.getEmail();
            String password = new AES128(USER_INFO_PASSWORD_KEY).encrypt(signInInput.getPassword());
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Response<>(NOT_FOUND_USER));
            } else if (!user.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Response<>(FAILED_TO_SIGN_IN));
            } else {
                userDB = user;
            }
        } catch (Exception e) {
            log.error("[users/signin/post] database error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>(DATABASE_ERROR));
        }

        // 3. token 생성
        String accessToken;
        String refreshToken;
        try {
            accessToken = jwtService.createAccessToken(userDB.getId());
            refreshToken = jwtService.createRefreshToken(userDB.getId());
            if (accessToken.isEmpty() || refreshToken.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Response<>(FAILED_TO_CREATE_TOKEN));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(FAILED_TO_CREATE_TOKEN));
        }

        // 4. 결과 return
        SignInOutput signInOutput = SignInOutput.builder().userId(userDB.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken).build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Response<>(signInOutput, SUCCESS_SIGN_IN));
    }

    @Override
    @Transactional
    public ResponseEntity<Response<SignUpOutput>> signUp(SignUpInput signUpInput) {
        // 1. 값 형식 체크
        System.out.println("Grade "+signUpInput.getGrade());
        if (signUpInput == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(NO_VALUES));
        }
        if (!ValidationCheck.isValid(signUpInput.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(BAD_EMAIL_VALUE));
        }
        if (!ValidationCheck.isValid(signUpInput.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(BAD_PASSWORD_VALUE));
        }
        if (!ValidationCheck.isValid(signUpInput.getNickname())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(BAD_NAME_VALUE));
        }
        if (!ValidationCheck.isValid(signUpInput.getGrade())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(BAD_GRADE_VALUE));
        }
        // 2. 유저 생성
        User user;
        try {
            String email = signUpInput.getEmail();
            String nickname = signUpInput.getNickname();
            boolean existUsers = userRepository.existsByEmail(email);
            boolean existNickname = userRepository.existsByNickname(nickname);
            String password = new AES128(USER_INFO_PASSWORD_KEY).encrypt(signUpInput.getPassword());
            user = User.builder().email(email).password(password)
                    .nickname(nickname).grade(signUpInput.getGrade()).build();
            System.out.println("user : " + user);
            if (existUsers) { // 이메일 중복 제어
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Response<>(EXISTS_EMAIL));
            } else if (existNickname) { // 닉네임 중복 제어
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Response<>(EXISTS_NICKNAME));
            } else {
                user = userRepository.save(user);
            }

        } catch (Exception e) {
            log.error("[users/signup/post] database error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>(DATABASE_ERROR));
        }

        // 3. token 생성
        String accessToken;
        String refreshToken;
        try {
            accessToken = jwtService.createAccessToken(user.getId());
            refreshToken = jwtService.createRefreshToken(user.getId());
            if (accessToken.isEmpty() || refreshToken.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Response<>(FAILED_TO_CREATE_TOKEN));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(FAILED_TO_CREATE_TOKEN));
        }

        // 4. 결과 return
        SignUpOutput signUpOutput = SignUpOutput.builder().userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken).build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Response<>(signUpOutput, CREATED_USER));
    }

    @Override
    public ResponseEntity<Response<Object>> resign() {
        // 1. 로그인한 유저 정보 가져오기
        try {
            User loginUserDB = jwtService.getUser();
            if(loginUserDB == null) {
                log.error("[users/patch] NOT FOUND LOGIN USER error");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Response<>(NOT_FOUND_USER));
            }
            userRepository.delete(loginUserDB);
        } catch (Exception e) {
            log.error("[users/patch] database error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>(DATABASE_ERROR));
        }
        // 3. 결과 return
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Response<>(null, SUCCESS_DELETE_USER));
    }

    @Override
    public ResponseEntity<Response<EmailOutput>> sendMail(EmailInput emailInput) {
        // 1. 값 형식 체크
        if (emailInput == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(NO_VALUES));
        if (!ValidationCheck.isValid(emailInput.getEmail()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(BAD_REQUEST));
        // 2. 중복 메일인지 체크
        try {
            if (userRepository.existsByEmail(emailInput.getEmail())) {
                log.error("[users/email/post] DUPLICATE EMAIL error");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Response<>(EXISTS_EMAIL));
            }
        } catch (Exception e) {
            log.error("[users/email/post] database error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>(DATABASE_ERROR));
        }
        // 3. 인증 메일 전송
        EmailOutput emailOutput;
        try {
            String generatedString = RandomStringUtils.random(10, true, true);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailInput.getEmail());
            message.setSubject("회원가입 이메일 인증번호입니다.");
            message.setText(generatedString);
            emailOutput = EmailOutput.builder().auth(generatedString).build();
            mailSender.send(message);
        } catch (Exception e) {
            log.error("[users/email/post] send email error", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(FAILED_TO_SEND_EMAIL));
        }
        // 4. 결과 return
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Response<>(emailOutput, SUCCESS_SEND_MAIL));
    }

    @Override
    public ResponseEntity<Response<SelectProfileOutput>> selectProfile() {
        SelectProfileOutput selectProfileOutput;
        try {
            // 유저 id 가져오기
            int userId = jwtService.getUserId();
            selectProfileOutput = userRepository.findUserProfile(userId);
        } catch (Exception e) {
            log.error("[users/me/get] database error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>(DATABASE_ERROR));
        }
        if(selectProfileOutput==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(NOT_FOUND_USER));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Response<>(selectProfileOutput, SUCCESS_SELECT_PROFILE));
    }

    @Override
    public ResponseEntity<Response<Object>> updateProfile(UpdateProfileInput updateProfileInput) {
        try {
            // 유저 id 가져오기
            int userId = jwtService.getUserId();
            User selectUser = userRepository.findById(userId).orElse(null);

            // 입력 값 벨리데이션
            if (StringUtils.isNotBlank(updateProfileInput.getPassword()))
                selectUser.setPassword(new AES128(USER_INFO_PASSWORD_KEY).encrypt(updateProfileInput.getPassword()));
            if (StringUtils.isNotBlank(updateProfileInput.getNickname())) {
                boolean existNickname = userRepository.existsByNickname(updateProfileInput.getNickname());
                if (existNickname) { // 닉네임 중복 제어
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new Response<>(EXISTS_NICKNAME));
                }
                selectUser.setNickname(updateProfileInput.getNickname());
            }
            userRepository.save(selectUser);
        } catch (Exception e) {
            log.error("[users/patch] database error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>(DATABASE_ERROR));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Response<>(null, SUCCESS_UPDATE_PROFILE));
    }


    @Override
    public ResponseEntity<PageResponse<SelectUserOutput>> selectUserList(SelectUserInput selectUserInput) {
        log.info(selectUserInput.toString());
        // 1. 값 형식 체크
        if (selectUserInput == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PageResponse<>(NO_VALUES));
        if (!ValidationCheck.isValidPage(selectUserInput.getPage())
                || !ValidationCheck.isValidId(selectUserInput.getSize()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PageResponse<>(NO_VALUES));


        Pageable pageable = PageRequest.of(selectUserInput.getPage() - 1, selectUserInput.getSize());
        Page<SelectUserOutput> selectUserOutputs;
        try {
            selectUserOutputs = userRepository.findByDynamicQuery(selectUserInput, pageable);
        } catch (Exception e) {
            log.error("[admin/users] database error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PageResponse<>(DATABASE_ERROR));
        }
        if(selectUserOutputs==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PageResponse<>(NOT_FOUND_USER));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new PageResponse<>(selectUserOutputs, SUCCESS_SELECT_USER));
    }

    @Override
    public ResponseEntity<Response<Object>> deleteUserList(List<DeleteUserInput> userIdList) {
        // 1. 로그인한 유저 정보 가져오기
        try {
            List<Integer> idList = new LinkedList();
            for(DeleteUserInput input : userIdList) {
                if(userRepository.findById(input.getUserId()).orElse(null) == null){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new Response<>(NOT_FOUND_USER));
                }
                idList.add(input.getUserId());
            }
            userRepository.deleteAll(userRepository.findAllById(idList));
        } catch (Exception e) {
            log.error("[users/patch] database error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>(DATABASE_ERROR));
        }
        // 3. 결과 return
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Response<>(null, SUCCESS_DELETE_USER));
    }

}
