package com.sparta.back_office.util;

import com.sparta.back_office.entity.User;

import static com.sparta.back_office.entity.UserRoleEnum.USER;

public class utilTest {
    protected User saveUser() {
        Long userId = 1L;
        String username = "testus12";
        String email = "test@example.com";
        String password = "PassWo12";
        String intro = "Test intro";

        var mockUser = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(USER)
                .intro(intro)
                .build();
        return mockUser;
    }
}
