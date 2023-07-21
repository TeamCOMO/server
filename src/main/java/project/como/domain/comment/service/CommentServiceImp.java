package project.como.domain.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.comment.dto.CommentCreateRequestDto;
import project.como.domain.comment.dto.CommentDetailDto;
import project.como.domain.comment.dto.CommentResponseDto;
import project.como.domain.comment.model.Comment;
import project.como.domain.comment.reposiory.CommentRepository;
import project.como.domain.post.exception.PostNotFoundException;
import project.como.domain.post.model.Post;
import project.como.domain.post.repository.PostRepository;
import project.como.domain.user.model.User;
import project.como.domain.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImp implements CommentService
{
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void writeComment(String username, Long postId, CommentCreateRequestDto dto) {
        User findUser = userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 유저입니다."));
        Post findPost = postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException(postId));

        Comment comment = dto.toEntity(findUser, findPost);
        commentRepository.save(comment);
    }

    @Override
    public CommentDetailDto findComment(Long commentId) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 댓글입니다."));

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
                            .map(c -> new CommentDetailDto(c))
                            .collect(Collectors.toList()))
                            .build();
    }

    @Transactional
    @Override
    public void updateComment(String username, Long commentId, CommentDetailDto dto)  {
        userRepository.findByUsername(username).orElseThrow(()->
                new IllegalArgumentException("존재하지 않는 유저입니다"));
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 댓글입니다."));

        findComment.updateBody(dto.getBody());
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 댓글입니다."));

        commentRepository.delete(findComment);
    }

}