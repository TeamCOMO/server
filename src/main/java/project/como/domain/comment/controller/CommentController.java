package project.como.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.como.domain.comment.dto.CreateCommentRequest;
import project.como.domain.comment.model.Comment;
import project.como.domain.comment.service.CommentServiceImp;
import project.como.domain.post.model.Post;
import project.como.domain.user.repository.UserRepository;

@RestController
@RequiredArgsConstructor
public class CommentController {


    public final CommentServiceImp commentService;
    //public final PostService postService;  // post_id를 통헤 해당하는 댓글들 가져옴.

    @Data
    @AllArgsConstructor
    static class Result<T> {
        //private int count;
        private T data;
    }

    //게시물 댓글 생성
    @PostMapping("/posts/{post_id}/comments")
    public ResponseEntity<String> saveComment(@PathVariable("post_id") Long postId
            , @RequestBody @Valid CreateCommentRequest request) {
        // Post findPost = postService.id를 통해 찾기(postId);

        commentService.writeComment(postId, request);
        return ResponseEntity.ok("성공");
    }
    //게시물 댓글 수정
    //@PatchMapping("/posts/{post_id}/comments/{comment_id}")

    //게시물 댓글 삭제
    //게시물 댓글 목록 조회
    //게시물 댓글 조회(대댓글을 위해?)

    // 생성(userId, content
}
