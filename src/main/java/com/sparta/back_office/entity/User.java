package com.sparta.back_office.entity;

import com.sparta.back_office.dto.JwtUser;
import com.sparta.back_office.dto.ProfileRequestDto;
import jakarta.persistence.*;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "USERS")
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String intro;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;


    @OneToMany(mappedBy = "users", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Comment> commentList;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private PasswordChecking passwordChecking;


    @Builder
    public User(String username, String password, String email, UserRoleEnum role, String intro) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.intro = intro;
        this.role = role;
    }

    public void update(ProfileRequestDto req) {
        if(req.getUsername() != null) this.username = req.getUsername();
        if(req.getIntro() != null) this.intro = req.getIntro();
        if(req.getPassword() != null) this.password = req.getPassword();
    }

    public static User foreign(JwtUser jwtUser){
        var user = new User();
        user.id = jwtUser.id();
        user.role = jwtUser.role();
        user.username = jwtUser.username();
        return user;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}