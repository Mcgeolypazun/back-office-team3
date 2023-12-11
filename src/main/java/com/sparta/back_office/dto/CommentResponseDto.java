package com.sparta.back_office.dto;

import com.sparta.back_office.entity.Comment;
import com.sparta.back_office.entity.User;
import com.sparta.back_office.post.entity.PostEntity;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentResponseDto extends MsgResponseDto{
    PostEntity post;
    String text;
    User user;
    LocalDateTime createTime;
    LocalDateTime modifyTime;


    public CommentResponseDto(Comment comment) {
        this.post=comment.getPost();
        this.text = comment.getText();
        this.user= comment.getUsers();
        this.createTime=LocalDateTime.now();

    }
}
