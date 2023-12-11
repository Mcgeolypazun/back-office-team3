package com.sparta.back_office.post.repository;

import com.sparta.back_office.entity.User;
import com.sparta.back_office.post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findAllByOrderByCreatedAtDesc();

     Optional<PostEntity> findById(Long id);

}
