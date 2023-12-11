package com.sparta.back_office.service;
import com.sparta.back_office.dto.JwtUser;
import com.sparta.back_office.dto.LoginRequestDto;
import com.sparta.back_office.dto.SignupRequestDto;
import com.sparta.back_office.entity.PasswordChecking;
import com.sparta.back_office.entity.User;
import com.sparta.back_office.entity.UserRoleEnum;
import com.sparta.back_office.jwt.JwtUtil;
import com.sparta.back_office.repository.PasswordCheckingRepository;
import com.sparta.back_office.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordCheckingRepository passwordCheckingRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public JwtUser login(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("등록된 유저가 없습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return new JwtUser(user.getId(), user.getUsername(), user.getRole());
    }

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String intro = requestDto.getIntro();
        String email = requestDto.getEmail();

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인

        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }
        User user = new User(username, password, email, role, intro);
        //PasswordChecking passwordChecking = new PasswordChecking(password,user);
        // 사용자 등록
        User savedUser = userRepository.save(user);
        PasswordChecking passwordChecking = new PasswordChecking(password, savedUser);
        passwordCheckingRepository.save(passwordChecking);
    }
    public void signOut() {
        JwtUser loginUser = getLoginUser();
        userRepository.deleteById(loginUser.id());
    }

    public JwtUser getLoginUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof JwtUser) return (JwtUser) auth.getPrincipal();
        throw new AccessDeniedException("권한이 없습니다.");
    }
}


