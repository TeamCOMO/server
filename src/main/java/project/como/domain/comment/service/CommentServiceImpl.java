package project.como.domain.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.comment.dto.CommentCreateRequestDto;
import project.como.domain.comment.dto.CommentDetailDto;
import project.como.domain.comment.dto.CommentResponseDto;
import project.como.domain.comment.exception.CommentForbiddenAccessException;
import project.como.domain.comment.exception.CommentNotFoundException;
import project.como.domain.comment.model.Comment;
import project.como.domain.comment.repository.CommentRepository;
import project.como.domain.post.exception.PostNotFoundException;
import project.como.domain.post.model.Post;
import project.como.domain.post.repository.PostRepository;
import project.como.domain.user.exception.UserNotFoundException;
import project.como.domain.user.model.User;
import project.como.domain.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService
{
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void writeComment(String username, Long postId, CommentCreateRequestDto dto) {
        User findUser = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Post findPost = postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException(postId));

        Comment comment = dto.toEntity(findUser, findPost);
        commentRepository.save(comment);
    }

    @Override
    public CommentDetailDto findComment(Long commentId) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException(commentId));

        return CommentDetailDto.builder()
                .body(findComment.getBody())
                .build();
    }

    @Override
    public CommentResponseDto findComments(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);

        return  CommentResponseDto.builder()
                .comments(comments
                        .stream()
                        .map(CommentDetailDto::new)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    @Override
    public void updateComment(String username, Long commentId, CommentDetailDto dto) {
        User findUser = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException(commentId));

        if(!checkUpdate(findUser,findComment)){
            throw new CommentForbiddenAccessException();
        }

        findComment.updateBody(dto.getBody());
    }
    /**
     * 댓글 기능 추가
     * 수정은 댓글 작성자만 가능해야함.
     * 삭제는 댓글 작성자 혹은 게시물 작성자만 가능해야함.
     *
     * 문제는 현재
     */

    @Transactional
    @Override
    public void deleteComment(String username, Long commentId) {
        User findUser = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException());
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException(commentId));

        if(!checkDelete(findUser, findComment))
            throw new CommentForbiddenAccessException();

        commentRepository.delete(findComment);
    }

    // 댓글 권한 체크 로직 //
    @Override
    public boolean checkUpdate(User user, Comment comment) {
        //접근하는 사용자가 댓글을 작성한 사용자 맞는지
        return user.getId().equals(comment.getUser().getId());
    }

    @Override
    public boolean checkDelete(User user, Comment comment) {
        //댓글 작성자만 해당 댓글 삭제 가능 || 게시물 작성자는 모든 댓글을 삭제 가능
        return checkUpdate(user, comment) || user.getId().equals(comment.getPost().getUser().getId());
        //위 방법이 최선일까? 쿼리문 호출을 줄일 수 없을까
    }





}