package project.como.domain.comment.service;

import jakarta.persistence.EntityManager;
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
    private final EntityManager em;

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
    public void updateComment(String username, Long commentId, CommentDetailDto dto) throws Exception {
        em.clear();
        User findUser = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException(commentId));

        if(!checkUser(findUser,findComment)){
            throw new CommentForbiddenAccessException();
        }

        findComment.updateBody(dto.getBody());
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException(commentId));

        commentRepository.delete(findComment);
    }


    // 댓글 권한 체크 로직 //
    @Override
    public boolean checkUser(User user, Comment comment) {
        //접근하는 사용자가 댓글을 작성한 사용자 맞는지, 게시물 작성한 사용자 맞는지 확인     //쿼리문
        return user.getId().equals(comment.getUser().getId()) || user.getId().equals(comment.getPost().getUser().getId());
    }





}