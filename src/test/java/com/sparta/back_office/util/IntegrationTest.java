package com.sparta.back_office.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.back_office.dto.JwtAuthentication;
import com.sparta.back_office.dto.JwtUser;
import com.sparta.back_office.entity.UserRoleEnum;
import com.sparta.back_office.jwt.JwtUtil;
import com.sparta.back_office.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.sparta.back_office.entity.User;
import java.util.List;

import static com.sparta.back_office.entity.UserRoleEnum.USER;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class IntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper mapper;
//    @Autowired
//    protected PostRepository postRepository;
//    @Autowired
//    protected PostDynamicRepository postDynamicRepository;
//    @Autowired
//    protected CommentRepository commentRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected JwtUtil jwtUtil;

//    protected Post savePost(String title, String content, User user) {
//        return postRepository.saveAndFlush(Post.builder()
//                .title(title)
//                .user(user)
//                .content(content)
//                .build()
//        );
//    }
    protected User saveUser() {
        Long userId = 1L;
        String username = "testus12";
        String email = "test@example.com";
        String password = "PassWo12";
        String intro = "Test intro";

        var mockUser = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(USER)
                .intro(intro)
                .build();
        return mockUser;
    }

    protected User saveUser(String username, String password, String email, UserRoleEnum role) {
        User user = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(role)
                .intro("intro")
                .build();
        return userRepository.saveAndFlush(user);
    }

//    protected Comment saveComment(String content, User user, Post post) {
//        return commentRepository.saveAndFlush(Comment.builder()
//                .content(content)
//                .post(post)
//                .user(user)
//                .build()
//        );
//    }

    protected SecurityContext contextJwtUser(Long id, String username, UserRoleEnum role) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        var jwtUser = new JwtUser(id, username, role);
        var auth = new JwtAuthentication(jwtUser, List.of(new SimpleGrantedAuthority(jwtUser.role().name())));
        context.setAuthentication(auth);
        return context;
    }
}
