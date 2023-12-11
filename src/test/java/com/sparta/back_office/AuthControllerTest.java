package com.sparta.back_office;


import com.sparta.back_office.dto.JwtUser;
import com.sparta.back_office.entity.User;
import com.sparta.back_office.util.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;


import static com.sparta.back_office.entity.UserRoleEnum.USER;
import static com.sparta.back_office.jwt.JwtUtil.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DisplayName("인증 API 테스트")
class AuthControllerTest extends IntegrationTest {

    @DisplayName("회원 탈퇴 성공")
    @Test
    void signOutSuccess() throws Exception {
        // given
        User user = saveUser("한정석", "1234", "test@gmail.com", USER);
        SecurityContext context = contextJwtUser(user.getId(), user.getUsername(), user.getRole());
        // when // then
        mockMvc.perform(delete("/api/v1/signout")
                        .with(securityContext(context))
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.message").value("탈퇴했습니다.")
                );
    }

    @DisplayName("로그인하지 않은 유저라면 탈퇴할 수 없다.")
    @Test
    void signOutFailWhenNotLogin() throws Exception {
        // when // then
        mockMvc.perform(delete("/api/v1/signout")
                )
                .andDo(print())
                .andExpectAll(
                        status().isForbidden(),
                        jsonPath("$.message").value("권한이 없습니다.")
                );
    }

    @DisplayName("토큰 재발급 성공")
    @Test
    void issueToken() throws Exception {
        // given
        String token = jwtUtil.createToken(new JwtUser(10L, "test", USER), REFRESH_TYPE);
        // when // then
        mockMvc.perform(get("/api/v1/refresh")
                        .header(AUTHORIZATION_HEADER, token)
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        header().exists(AUTHORIZATION_HEADER),
                        header().exists(REFRESH_AUTHORIZATION_HEADER)
                );
    }

    @DisplayName("엑세스 토큰을 통한 토큰 재발급은 실패한다.")
    @Test
    void issueTokenWhenAccessToken() throws Exception {
        // given
        String token = jwtUtil.createToken(new JwtUser(10L, "test", USER), ACCESS_TYPE);
        // when // then
        mockMvc.perform(get("/api/v1/refresh")
                        .header(AUTHORIZATION_HEADER, token)
                )
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message").value("토큰이 유효하지 않습니다.")
                );
    }

    @DisplayName("토큰이 없다면 토큰 재발급은 실패한다.")
    @Test
    void issueTokenWhenNotExistToken() throws Exception {
        // given
        // when // then
        mockMvc.perform(get("/api/v1/refresh"))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message").value("토큰이 유효하지 않습니다.")
                );
    }
}