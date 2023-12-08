package com.sparta.back_office.repository;

import com.sparta.back_office.entity.PasswordChecking;
import com.sparta.back_office.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordCheckingRepository extends JpaRepository<PasswordChecking,Long> {

    PasswordChecking findByUser(User user);
}
