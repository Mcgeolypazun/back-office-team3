package com.sparta.back_office.controller;

import com.sparta.back_office.dto.CommentRequestDto;
import com.sparta.back_office.dto.CommentResponseDto;
import com.sparta.back_office.dto.MsgResponseDto;
import com.sparta.back_office.entity.Comment;
import com.sparta.back_office.post.dto.PostResponseDto;
import com.sparta.back_office.security.UserDetailsImpl;
import com.sparta.back_office.service.CommentService;
import java.util.concurrent.RejectedExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static java.rmi.server.LogStream.log;

@Slf4j(topic = "CommentController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable Long commentId){
        log.info("postComment 실행");
        log.info("CommentRequestDto commentRequestDto.getPostId() = " + commentId);

        Comment comment = commentService.getUserComment2(commentId);
        CommentResponseDto responseDTO = new CommentResponseDto(comment);
        return ResponseEntity.status(201).body(responseDTO);
    }
    @PostMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> postComment(@RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long commentId){
        log.info("postComment 실행");
        log.info("CommentRequestDto commentRequestDto.getPostId() = " + commentId);
        CommentResponseDto responseDTO = commentService.createComment(commentRequestDto, userDetails,commentId);
        return ResponseEntity.status(201).body(responseDTO);
    }
    @PutMapping("/{commentId}")
    public ResponseEntity<MsgResponseDto> update(@RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long commentId){

        try{
            CommentResponseDto responseDto = commentService.updateComment(commentRequestDto, userDetails, commentId);
            return ResponseEntity.ok().body(responseDto);
        } catch (IllegalArgumentException | RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new MsgResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<MsgResponseDto> delete(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try{
            commentService.deleteComment(commentId, userDetails);
            return ResponseEntity.ok().body(new MsgResponseDto("정상적으로 삭제 되었습니다.", HttpStatus.OK.value()));
        } catch (IllegalArgumentException | RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new MsgResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
}
