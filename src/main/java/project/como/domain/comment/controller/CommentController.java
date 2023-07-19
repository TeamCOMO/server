package project.como.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.como.domain.comment.dto.CommentCreateRequestDto;
import project.como.domain.comment.dto.CommentDto;
import project.como.domain.comment.dto.CommentUpdateDto;
import project.como.domain.comment.model.Comment;
import project.como.domain.comment.service.CommentServiceImp;
import project.como.domain.post.dto.PostDetailResponseDto;
import project.como.domain.post.service.PostService;
import project.como.global.auth.model.CurrentUser;

import java.util.List;

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
    }

    //게시물 댓글 생성
    @PostMapping("/posts/{post_id}/comment")
    public ResponseEntity<String> createComment(@PathVariable("post_id") Long postId
            , @RequestBody @Valid CommentCreateRequestDto dto, @CurrentUser String username) { //@CurrentUser를 통해 인증된 username 가져옴

        commentService.writeComment(username, postId, dto);

        return ResponseEntity.ok("댓글 생성");
        /**
         * 궁금증 : 전반적으로 Comment 필드들 중 어디까지 보여줘야 하는지 모르겠음.
         * 해결 : 개인적으로는 postId, username을 넘기고 싶은데 postRepository-> postId를 찾는 메서드 유무를 모름.
         *
         * 1) 생성한 댓글을 게시물이 보여질 때, 위와 같은 방식이 아니라 ResponseEntity<CommentCreateResponseDto> 형식으로 Dto를 넘기는 것이 효율적인가?
         * 2) CommentCreateResponseDto를 넘겨주려 하는데 필드를 post, user 대신 postId, username를 넘겨주는 것이 맞을까? 아님 이에 대한 값이 필요가 없어도 될까?
         */
    }

    //게시물 댓글 목록 조회
    @GetMapping("/posts/{post_id}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable("post_id") Long postId){

        List<CommentDto> comments = commentService.findComments(postId);

        return ResponseEntity.ok(comments);
    }

    //게시물 댓글 조회(대댓글을 위해?)

    //게시물 댓글 수정
    @PatchMapping("/comments/{comment_id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("comment_id") Long commentId,
                                                @RequestBody @Valid CommentUpdateDto dto){
        CommentDto commentDto = commentService.updateComment(commentId, dto);

        return ResponseEntity.ok(commentDto);
    }

    //게시물 댓글 삭제
    @DeleteMapping("/comments/{comment_id}")
    public ResponseEntity<String> deleteComment(@PathVariable("comment_id") Long commentId){
        commentService.deleteComment(commentId);

        return ResponseEntity.ok("id : " + commentId.toString() +", 댓글 삭제");
    }


}
