package com.sparta.back_office.service;

import com.sparta.back_office.dto.UsersResponseDto;
import com.sparta.back_office.entity.User;
import com.sparta.back_office.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "CommentService")
@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    public List<UsersResponseDto> getUserList() {
        List<User> userList = userRepository.findAll();
        //log.info(" userList 이름 = "+userList.get(0).getUsername());


        List<UsersResponseDto> usersResponseDtoList = new ArrayList<>();

        for (User user : userList) {
            UsersResponseDto usersResponseDto = new UsersResponseDto(); // 새로운 객체 생성
            usersResponseDto.setId(user.getId());
            usersResponseDto.setUsername(user.getUsername());
            usersResponseDto.setPassword(user.getPassword());
            usersResponseDto.setIntro(user.getIntro());
            usersResponseDto.setEmail(user.getEmail());
            usersResponseDto.setRole(user.getRole());
            usersResponseDtoList.add(usersResponseDto);
        }

        return usersResponseDtoList;
    }

}
