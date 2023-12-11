package com.sparta.back_office.service;

import com.sparta.back_office.dto.ProfileRequestDto;
import com.sparta.back_office.dto.ProfileResponseDto;
import com.sparta.back_office.entity.PasswordChecking;
import com.sparta.back_office.entity.User;
import com.sparta.back_office.repository.PasswordCheckingRepository;
import com.sparta.back_office.repository.UserRepository;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Base64;
import java.util.NoSuchElementException;
@Slf4j(topic = "ProfileService")
@RequiredArgsConstructor
@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordCheckingRepository passwordCheckingRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret.key}")
    String secretKey;


    @Transactional
    public ProfileResponseDto updateProfile(Long id, ProfileRequestDto requestDto) {
        User profileUser = userRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        profileUser.update(requestDto);
        return new ProfileResponseDto(profileUser);
    }

    public ProfileResponseDto getProfile(Long id) {
        // 해당 ID에 대한 프로필을 찾음
        User user = findProfile(id);
        // ProfileUser를 ProfileResponseDto로 변환하여 반환
        return new ProfileResponseDto(user);
    }

    private User findProfile(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("프로파일이 존재하지 않습니다."));
    }

    @Transactional
    public void updateUserPassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        PasswordChecking passwordChecking = passwordCheckingRepository.findByUser(user);
        log.info("passwordChecking.getFirstPassword = "+ passwordChecking.getFirstPassword());
        log.info("passwordChecking.getSecondPassword = "+ passwordChecking.getSecondPassword());
        log.info("passwordChecking.getThirdPassword = "+ passwordChecking.getThirdPassword());

        validateNewPassword(user, newPassword);
        String password = passwordEncoder.encode(newPassword);
        user.setPassword(password);
        passwordChecking.setPassword(password);
        passwordCheckingRepository.save(passwordChecking);
        userRepository.save(user);
    }

    private void validateNewPassword(User user, String password) {
        PasswordChecking lastThreePasswords = passwordCheckingRepository.findByUser(user);
        log.info("UserPassword = "+ user.getPassword());
        log.info("lastThreePasswords.getFirstPassword = "+ lastThreePasswords.getFirstPassword());
        log.info("lastThreePasswords.getSecondPassword = "+ lastThreePasswords.getSecondPassword());
        log.info("lastThreePasswords.getThirdPassword = "+ lastThreePasswords.getThirdPassword());
        if(passwordEncoder.matches(password,lastThreePasswords.getFirstPassword())
                || passwordEncoder.matches(password,lastThreePasswords.getSecondPassword())
                || passwordEncoder.matches(password,lastThreePasswords.getThirdPassword())){
            throw new IllegalArgumentException("이전에 사용한 비밀번호는 사용할 수 없습니다.");
        }
    }
}

