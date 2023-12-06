package com.sparta.back_office.post.dto;

import lombok.Getter;

@Getter
public class PostUpdateRequestDto {
    private String title;
    private String author;
    private String content;
}
