package com.sparta.back_office.dto;

import com.sparta.back_office.entity.Comment;
import com.sparta.back_office.entity.PasswordChecking;
import com.sparta.back_office.entity.User;
import com.sparta.back_office.entity.UserRoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class UsersResponseDto {

    //private List<User> userList;

    private Long id;


    private String username;


    private String password;


    private String email;


    private String intro;


    private UserRoleEnum role;



    private List<Comment> commentList;


    private PasswordChecking passwordChecking;
}
