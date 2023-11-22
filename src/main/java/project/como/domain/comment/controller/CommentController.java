package project.como.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.como.domain.comment.dto.CommentCreateRequestDto;
import project.como.domain.comment.dto.CommentCreateResponseDto;
import project.como.domain.comment.dto.CommentDetailDto;
import project.como.domain.comment.dto.CommentResponseDto;
import project.como.domain.comment.service.CommentService;
import project.como.global.auth.model.CurrentUser;

@RestController @Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class CommentController {


    public final CommentService commentService;

    //게시물 댓글 생성
    @PostMapping("/post/{post_id}/comment")
    public ResponseEntity<CommentCreateResponseDto> createComment(@PathVariable("post_id") Long postId
            , @RequestBody @Valid CommentCreateRequestDto dto, @CurrentUser String username) { //@CurrentUser를 통해 인증된 username 가져옴
        CommentCreateResponseDto commentDto = commentService.create(username, postId, dto);
        //log.info("dto = {}", commentDto.getId());
        //log.info("dto = {}", commentDto.getParentId());
        return ResponseEntity.ok().body(commentDto);
    }

    //게시물 댓글 목록 조회
    @GetMapping("/post/{post_id}/comments")
    public ResponseEntity<CommentResponseDto> getComments(@PathVariable("post_id") Long postId){
        CommentResponseDto comments = commentService.getListById(postId);

        return ResponseEntity.ok().body(comments);
    }

    //게시물 댓글 수정
    @PatchMapping("/comment/{comment_id}")
    public ResponseEntity<String> updateComment(@CurrentUser String username,
                                                                  @PathVariable("comment_id") Long commentId,
                                                                  @RequestBody @Valid CommentDetailDto dto){
        commentService.modifyById(username, commentId, dto);

        return ResponseEntity.ok().body("success");
    }

    //게시물 댓글 삭제
    @DeleteMapping("/comment/{comment_id}")
    public ResponseEntity<String> deleteComment(@CurrentUser String username,
                                                @PathVariable("comment_id") Long commentId){
        commentService.deleteById(username, commentId);

        return ResponseEntity.ok().body("success");
    }

}
