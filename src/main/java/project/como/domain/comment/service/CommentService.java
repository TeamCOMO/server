package project.como.domain.comment.service;

import project.como.domain.comment.dto.CommentCreateRequestDto;
import project.como.domain.comment.dto.CommentDetailDto;
import project.como.domain.comment.dto.CommentResponseDto;
import project.como.domain.comment.model.Comment;
import project.como.domain.user.model.User;

public interface CommentService {
    public void writeComment(String userId, Long postId, CommentCreateRequestDto dto); //댓글 생성
    public void updateComment(String username, Long commentId, CommentDetailDto dto); // 댓글 수정
    public CommentResponseDto findComments(Long postId); // 게시물에 작성한 모든 댓글

    public CommentDetailDto findComment(Long commentId);

    public void deleteComment(String username, Long commentId); // 댓글 삭제



    public boolean checkUpdate(User user, Comment comment);

    public boolean checkDelete(User user, Comment comment);

    //public Comment findComment(); // 대댓글을 달기 위한 용도
}