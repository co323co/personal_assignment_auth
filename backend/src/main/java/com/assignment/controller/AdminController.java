package com.assignment.controller;

import com.assignment.dto.admin.deleteuser.DeleteUserInput;
import com.assignment.dto.admin.selectuser.SelectUserInput;
import com.assignment.dto.admin.selectuser.SelectUserOutput;
import com.assignment.response.PageResponse;
import com.assignment.response.Response;
import com.assignment.service.JwtService;
import com.assignment.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.assignment.response.ResponseStatus.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UserService userService;
    private final JwtService jwtService;

    /**
     * 유저 리스트 조회 API
     * [GET] /api/admin/users?&page=&size=
     * @return ResponseEntity<PageResponse<SelectCommentOutput>>
     */
    // Params
    @GetMapping("/users")
    public ResponseEntity<PageResponse<SelectUserOutput>> getUserList(SelectUserInput selectUserInput) {
        log.info("[GET] /api/admin/users?&page=&size=...");
        log.info(selectUserInput.toString());
        return userService.selectUserList(selectUserInput);
    }

    /**
     * 유저 리스트 삭제 API
     * [POST] /api/admin/users
     * @return ResponseEntity<PageResponse<>>
     */
    // Params
    @DeleteMapping("/users")
    public ResponseEntity<Response<Object>> deleteUserList(@RequestBody List<DeleteUserInput> userIdList) {
        log.info("[DELETE] /api/admin/users");
        return userService.deleteUserList(userIdList);
    }

}
