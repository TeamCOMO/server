package project.como.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.como.domain.comment.dto.CommentCreateRequestDto;
import project.como.domain.comment.dto.CommentDetailDto;
import project.como.domain.comment.dto.CommentResponseDto;
import project.como.domain.comment.service.CommentServiceImpl;
import project.como.global.auth.model.CurrentUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class CommentController {


    public final CommentServiceImpl commentService;

    //게시물 댓글 생성
    @PostMapping("/post/{post_id}/comment")
    public ResponseEntity<String> createComment(@PathVariable("post_id") Long postId
            , @RequestBody @Valid CommentCreateRequestDto dto, @CurrentUser String username) { //@CurrentUser를 통해 인증된 username 가져옴
        commentService.writeComment(username, postId, dto);

        return ResponseEntity.ok().body("success");
    }

    //게시물 댓글 목록 조회
    @GetMapping("/post/{post_id}/comments")
    public ResponseEntity<CommentResponseDto> getComments(@PathVariable("post_id") Long postId){
        CommentResponseDto comments = commentService.findComments(postId);

        return ResponseEntity.ok().body(comments);
    }

    //게시물 댓글 수정
    @PatchMapping("/comment/{comment_id}")
    public ResponseEntity<String> updateComment(@CurrentUser String username,
                                                                  @PathVariable("comment_id") Long commentId,
                                                                  @RequestBody @Valid CommentDetailDto dto){
        commentService.updateComment(username, commentId, dto);

        return ResponseEntity.ok().body("success");
    }

    //게시물 댓글 삭제
    @DeleteMapping("/comment/{comment_id}")
    public ResponseEntity<String> deleteComment(@CurrentUser String username,
                                                @PathVariable("comment_id") Long commentId){
        commentService.deleteComment(username, commentId);

        return ResponseEntity.ok().body("success");
    }

}
