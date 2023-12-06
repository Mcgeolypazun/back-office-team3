package com.sparta.back_office.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequestDto {
    private String username;
    private String intro;
    private String email;
}
