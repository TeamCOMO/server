package project.como.domain.comment.service;

import project.como.domain.comment.dto.CommentCreateRequestDto;
import project.como.domain.comment.dto.CommentDto;
import project.como.domain.comment.dto.CommentUpdateDto;
import project.como.domain.comment.model.Comment;

import java.util.List;

public interface CommentService {
    public void writeComment(String userId, Long postId, CommentCreateRequestDto dto); //댓글 생성
    public CommentDto updateComment(Long commentId, CommentUpdateDto dto) ; // 댓글 수정
    public List<CommentDto> findComments(Long postId); // 게시물에 작성한 모든 댓글

    public CommentDto findComment(Long commentId);

    public void deleteComment(Long commentId) ; // 댓글 삭제

    //public Comment findComment(); // 대댓글을 달기 위한 용도
}
