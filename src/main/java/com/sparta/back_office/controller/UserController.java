package com.sparta.back_office.controller;

import com.sparta.back_office.dto.*;
import com.sparta.back_office.entity.UserRoleEnum;
import com.sparta.back_office.jwt.JwtUtil;
import com.sparta.back_office.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.sparta.back_office.jwt.JwtUtil.*;

@Slf4j
@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<MsgResponseDto> signup(@Valid @RequestBody SignupRequestDto userRequestDto) {
        try {
            userService.signup(userRequestDto);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(new MsgResponseDto("중복된 username 입니다.", HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(new MsgResponseDto("회원가입 성공", HttpStatus.CREATED.value()));
    }

    @PostMapping("/login")
    public ResponseEntity<MsgResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            JwtUser user = userService.login(loginRequestDto);
            String accessToken = jwtUtil.createToken(user, ACCESS_TYPE);
            String refreshToken = jwtUtil.createToken(user, REFRESH_TYPE);

            return ResponseEntity.ok()
                    .header(AUTHORIZATION_HEADER, accessToken)
                    .header(REFRESH_AUTHORIZATION_HEADER, refreshToken)
                    .body(new MsgResponseDto("로그인 성공", HttpStatus.OK.value()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new MsgResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

    }

    @DeleteMapping("/signout")
    public ResponseEntity<?> signOut() {
        userService.signOut();
        return ResponseEntity.ok(new MessageDto("탈퇴했습니다."));
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader(value = AUTHORIZATION_HEADER, required = false) String token) {
        Optional<JwtUser> bearerToken = jwtUtil.getJwtUser(token, REFRESH_TYPE);
        if (bearerToken.isEmpty())
            return ResponseEntity.badRequest().body(new MessageDto("토큰이 유효하지 않습니다."));

        JwtUser user = bearerToken.get();

        String accessToken = jwtUtil.createToken(user, ACCESS_TYPE);
        String refreshToken = jwtUtil.createToken(user, REFRESH_TYPE);

        return ResponseEntity.ok()
                .header(AUTHORIZATION_HEADER, accessToken)
                .header(REFRESH_AUTHORIZATION_HEADER, refreshToken)
                .build();
    }


}
