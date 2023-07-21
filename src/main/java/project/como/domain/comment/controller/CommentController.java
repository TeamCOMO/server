package project.como.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.como.domain.comment.dto.CommentCreateRequestDto;
import project.como.domain.comment.dto.CommentDetailDto;
import project.como.domain.comment.dto.CommentResponseDto;
import project.como.domain.comment.service.CommentServiceImp;
import project.como.domain.post.service.PostService;
import project.como.global.auth.model.CurrentUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {


    public final CommentServiceImp commentService;
    public final PostService postService;

    @Data
    @AllArgsConstructor
    static class Result<T> {
        //private int count;
        private T data;
    } // 적용 안 함

    //게시물 댓글 생성
    @PostMapping("/post/{post_id}/comment")
    public ResponseEntity<String> createComment(@PathVariable("post_id") Long postId
            , @RequestBody @Valid CommentCreateRequestDto dto, @CurrentUser String username) { //@CurrentUser를 통해 인증된 username 가져옴

        commentService.writeComment(username, postId, dto);

        return ResponseEntity.ok().body("댓글 생성"); // 일관성 통일
    }

    //게시물 댓글 목록 조회
    @GetMapping("/post/{post_id}/comments")
    public ResponseEntity<CommentResponseDto> getComments(@PathVariable("post_id") Long postId){

        CommentResponseDto comments = commentService.findComments(postId);

        return ResponseEntity.ok().body(comments);
    }

    //게시물 댓글 조회(대댓글을 위해?)

    //게시물 댓글 수정
    @PatchMapping("/comment/{comment_id}")
    public ResponseEntity<String> updateComment(@CurrentUser String username, //수정 권환 인증
                                                                  @PathVariable("comment_id") Long commentId,
                                                                  @RequestBody @Valid CommentDetailDto dto){
        commentService.updateComment(username, commentId, dto);

        return ResponseEntity.ok().body("댓글 수정");
    }

    //게시물 댓글 삭제
    @DeleteMapping("/comment/{comment_id}")
    public ResponseEntity<String> deleteComment(@PathVariable("comment_id") Long commentId){
        commentService.deleteComment(commentId);

        return ResponseEntity.ok().body("id : " + commentId.toString() +", 댓글 삭제");
    }

}
