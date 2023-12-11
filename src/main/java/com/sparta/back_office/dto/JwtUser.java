package com.sparta.back_office.dto;

import com.sparta.back_office.entity.User;
import com.sparta.back_office.entity.UserRoleEnum;

public record JwtUser(
        Long id,
        String username,
        UserRoleEnum role
) {

    public static JwtUser of(User user) {
        return new JwtUser(user.getId(), user.getUsername(), user.getRole());
    }
}

