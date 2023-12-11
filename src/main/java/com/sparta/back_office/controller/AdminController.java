package com.sparta.back_office.controller;


import com.sparta.back_office.dto.UserInfoDto;
import com.sparta.back_office.dto.UsersResponseDto;
import com.sparta.back_office.entity.User;
import com.sparta.back_office.entity.UserRoleEnum;
import com.sparta.back_office.post.dto.PostResponseDto;
import com.sparta.back_office.repository.UserRepository;
import com.sparta.back_office.security.UserDetailsImpl;
import com.sparta.back_office.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "AdminController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AdminController {

    private final AdminService adminService;



    @GetMapping("/user-info")
    @ResponseBody
    public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUser().getUsername();
        UserRoleEnum role = userDetails.getUser().getRole();
        boolean isAdmin = (role == UserRoleEnum.ADMIN);

        return new UserInfoDto(username, isAdmin);
    }
    @GetMapping("/users")
    @ResponseBody
    public  ResponseEntity<List<UsersResponseDto>> getUserInfoList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<UsersResponseDto> usersResponseDtoList =  adminService.getUserList();
        return ResponseEntity.ok(usersResponseDtoList);
    }
//    @GetMapping
//    public ResponseEntity<List<PostResponseDto>> getPosts() {
//        List<PostResponseDto> responseDto = postService.getPosts();
//        return ResponseEntity.ok(responseDto);
//    }

}
