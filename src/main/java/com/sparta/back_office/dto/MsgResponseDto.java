package com.sparta.back_office.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MsgResponseDto {
    private String msg;
    private int statusCode;
}
