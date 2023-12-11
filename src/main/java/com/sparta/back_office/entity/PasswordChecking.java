package com.sparta.back_office.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PasswordChecking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String firstPassword ="";
    private String secondPassword="";
    private String thirdPassword="";

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;



    public void setPassword(String password) {
        thirdPassword = secondPassword;
        secondPassword = firstPassword;
        this.firstPassword = password;
    }

    public PasswordChecking(String password, User user) {
        this.firstPassword = password;
        this.user = user;
    }
}
