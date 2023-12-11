package com.sparta.back_office.security;

import com.sparta.back_office.entity.User;
import com.sparta.back_office.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Slf4j(topic = "UserDetailsServiceImpl")
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        log.info("loadUserByUsername(String username) : "+ id );
        Long longId = Long.valueOf(id);
        log.info("user 존재 확인 : "+ userRepository.findById(longId) );
        User user = userRepository.findById(longId)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + longId));

        return new UserDetailsImpl(user);
    }
}