package com.sparta.back_office.post.dto;



import com.sparta.back_office.post.entity.PostEntity;

import java.time.LocalDateTime;

public record PostResponseDto(
    Long id,
    String title,
    String author,
    String content,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt
) {

    public PostResponseDto(PostEntity savePost) {
        this(
            savePost.getId(),
            savePost.getTitle(),
            savePost.getAuthor(),
            savePost.getContents(),
            savePost.getCreatedAt(),
            savePost.getModifiedAt()
        );
    }
}