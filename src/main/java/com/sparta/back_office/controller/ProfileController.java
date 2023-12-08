package com.sparta.back_office.controller;

import com.sparta.back_office.dto.MsgResponseDto;
import com.sparta.back_office.dto.PasswordUpdateRequestDto;
import com.sparta.back_office.dto.ProfileRequestDto;
import com.sparta.back_office.dto.ProfileResponseDto;
import com.sparta.back_office.service.ProfileService;
import com.sparta.back_office.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class ProfileController {

    private final ProfileService profileService;


    @GetMapping("/{id}")
    public ProfileResponseDto getProfileById(@PathVariable Long id) {
        return profileService.getProfile(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody ProfileRequestDto requestDto) {
        try {
            ProfileResponseDto response = profileService.updateProfile(id, requestDto);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new MsgResponseDto("Failed to update profile",500));
        }
    }

    @PutMapping("/password/{Id}")
    public ResponseEntity<MsgResponseDto> updateUserPassword(@PathVariable Long Id,
                                                             @RequestBody PasswordUpdateRequestDto requestDto) {
        profileService.updateUserPassword(Id, requestDto.getNewPassword());
        return ResponseEntity.ok(new MsgResponseDto("비밀번호 변경 성공하였습니다.",200));
    }
}

