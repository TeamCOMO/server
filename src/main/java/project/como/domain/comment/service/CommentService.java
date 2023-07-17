package project.como.domain.comment.service;

import project.como.domain.comment.dto.CreateCommentRequest;
import project.como.domain.comment.model.Comment;

import java.util.List;

public interface CommentService {
    public void writeComment(String userId, Long postId, CreateCommentRequest dto); //댓글 생성
    public Comment updateComment(Long commentId, String body) throws Exception; // 댓글 수정
    public List<Comment> findComments(); // 게시물에 작성한 모든 댓글

    public void deleteComment(Long commentId) throws Exception; // 댓글 삭제

    //public Comment findComment(); // 대댓글을 달기 위한 용도
}
