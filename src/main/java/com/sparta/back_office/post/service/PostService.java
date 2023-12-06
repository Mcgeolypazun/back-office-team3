package com.sparta.back_office.post.service;


import com.sparta.back_office.entity.User;
import com.sparta.back_office.post.dto.PostResponseDto;
import com.sparta.back_office.post.exception.PostNotFoundException;
import com.sparta.back_office.post.dto.PostAddRequestDto;
import com.sparta.back_office.post.dto.PostUpdateRequestDto;
import com.sparta.back_office.post.entity.PostEntity;
import com.sparta.back_office.post.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostJpaRepository postJpaRepository;

    public PostResponseDto addPost(PostAddRequestDto requestDto) {
        // Dto -> Entity
        PostEntity postEntity = new PostEntity(requestDto);
        PostEntity savePost = postJpaRepository.save(postEntity);
        return new PostResponseDto(savePost);
    }

    public PostResponseDto getPost(Long postId) {
        PostEntity postEntity = getPostEntity(postId);
        return new PostResponseDto(postEntity);
    }

    public List<PostResponseDto> getPosts() {
        return postJpaRepository.findAllByOrderByCreatedAtDesc().stream()
            .map(PostResponseDto::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public PostResponseDto updatePost(Long postId, PostUpdateRequestDto requestDto, User user) {
        PostEntity postEntity = getPostEntity(postId);
        postEntity.update(requestDto);
        return new PostResponseDto(postEntity);
    }

    public void deletePost(Long postId, User user) {
        PostEntity postEntity = getPostEntity(postId);
        postJpaRepository.delete(postEntity);
    }

    private PostEntity getPostEntity(Long postId) {
        return postJpaRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException("해당 게시글을 찾을 수 없습니다."));
    }


}
