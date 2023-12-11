package com.sparta.back_office.service;

import com.sparta.back_office.dto.CommentRequestDto;
import com.sparta.back_office.dto.CommentResponseDto;
import com.sparta.back_office.entity.Comment;
import com.sparta.back_office.entity.User;
import com.sparta.back_office.post.dto.PostResponseDto;
import com.sparta.back_office.post.entity.PostEntity;
import com.sparta.back_office.post.repository.PostJpaRepository;
import com.sparta.back_office.repository.CommentRepository;
import com.sparta.back_office.security.UserDetailsImpl;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.concurrent.RejectedExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j(topic = "CommentService")
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostJpaRepository postRepository;
    private User user;
    Comment comment;
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails,Long id) {

        user = userDetails.getUser();
        log.info("user.getId() = "+user.getId());
        log.info("user.getUsername() = "+ user.getUsername());
        log.info("user.getPassword() = "+ user.getPassword());

        Optional<PostEntity> optionalPost = postRepository.findById(id);
/*
        Comment comment = new Comment(commentRequestDto);
        post.addComment(comment);
        */
        if(optionalPost.isPresent()){
            comment = new Comment(commentRequestDto, optionalPost.get());
            log.info("id "+ optionalPost.get().getId());
            log.info("Title "+ optionalPost.get().getTitle());
            log.info("Author "+ optionalPost.get().getAuthor());
            log.info("Contents "+ optionalPost.get().getContents());
            log.info("commentRequestDto "+ commentRequestDto.getText());

            optionalPost.get().addComment(comment);
        }else{
            log.error("id "+ optionalPost.get().getId());
            log.error("Title "+ optionalPost.get().getTitle());
            log.error("Author "+ optionalPost.get().getAuthor());
            log.error("Contents "+ optionalPost.get().getContents());
        }
        log.info("commentRepository.save(comment) 시작 ");
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }
    @Transactional
    public CommentResponseDto updateComment(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails, Long commentId) {
        Comment comment = getUserComment(commentId, userDetails.getUser());

        comment.updateComment(commentRequestDto.getText());

        return new CommentResponseDto(comment);
    }
    @Transactional
    public void deleteComment( Long commentId, UserDetailsImpl userDetails) {
        Comment comment = getUserComment(commentId, userDetails.getUser());
        commentRepository.delete(comment);
    }
    public Comment getUserComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글 ID 입니다."));

        if(!user.getId().equals(comment.getUsers().getId())) {
            throw new RejectedExecutionException("작성자만 수정할 수 있습니다.");
        }
        return comment;
    }

    public Comment getUserComment2(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글 ID 입니다."));

        return comment;
    }

}
