package com.sparta.back_office.post.controller;

import com.sparta.back_office.post.exception.AuthorizeException;
import com.sparta.back_office.post.exception.PostNotFoundException;
import com.sparta.back_office.post.dto.PostAddRequestDto;
import com.sparta.back_office.post.dto.PostResponseDto;
import com.sparta.back_office.post.dto.PostUpdateRequestDto;
import com.sparta.back_office.post.exception.ErrorResponseDto;
import com.sparta.back_office.post.service.PostService;
import com.sparta.back_office.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "PostController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> addPost(
        @RequestBody PostAddRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        log.info("권한이 추가 되었는지 확인 UserDetailsImpl userDetails.getAuthorities() = " + userDetails.getAuthorities());
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))|| userDetails.getAuthorities().stream().anyMatch(b -> b.getAuthority().equals("ROLE_USER"))) {
            PostResponseDto responseDto = postService.addPost(requestDto);
            log.info("Successfully obtained User: " + userDetails.getUser().getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } else {
            log.info(" 게시물을 추가할 수 없다." );
            throw new AuthorizeException("게시물을 추가할 수 있는 권한이 없습니다.");
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(
        @PathVariable Long postId
    ) {
        PostResponseDto responseDto = postService.getPost(postId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getPosts() {
        List<PostResponseDto> responseDto = postService.getPosts();
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
        @PathVariable Long postId,
        @Valid @RequestBody PostUpdateRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails.getUser().getId().equals(postId)) {
            PostResponseDto responseDto = postService.updatePost(postId, requestDto, userDetails.getUser());
            return ResponseEntity.ok(responseDto);
        } else {
            throw new AuthorizeException("게시물 업데이트 권한이 없습니다.");
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
        @PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails.getUser().getId().equals(postId)) {
            postService.deletePost(postId, userDetails.getUser());
            return ResponseEntity.noContent().build();
        } else {
            throw new AuthorizeException("게시물 삭제 권한이 없습니다.");
        }

    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> postNotFoundExceptionHandler(PostNotFoundException ex) {
//        System.err.println(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
            )
        );
    }

    @ExceptionHandler(AuthorizeException.class)
    public ResponseEntity<ErrorResponseDto> authorizeExceptionHandler(AuthorizeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            new ErrorResponseDto(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage()
            )
        );
    }
}