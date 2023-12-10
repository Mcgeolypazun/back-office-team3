package com.sparta.back_office.controller;

import com.sparta.back_office.dto.CommentRequestDto;
import com.sparta.back_office.dto.CommentResponseDto;
import com.sparta.back_office.dto.MsgResponseDto;
import com.sparta.back_office.post.dto.PostResponseDto;
import com.sparta.back_office.security.UserDetailsImpl;
import com.sparta.back_office.service.CommentService;
import java.util.concurrent.RejectedExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> postComment(@RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        CommentResponseDto responseDTO = commentService.createComment(commentRequestDto, userDetails);
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
