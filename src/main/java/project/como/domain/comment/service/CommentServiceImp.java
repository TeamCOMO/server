package project.como.domain.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.comment.dto.CommentCreateRequestDto;
import project.como.domain.comment.dto.CommentDto;
import project.como.domain.comment.dto.CommentUpdateDto;
import project.como.domain.comment.model.Comment;
import project.como.domain.comment.reposiory.CommentRepository;
import project.como.domain.post.model.Post;
import project.como.domain.post.repository.PostRepository;
import project.como.domain.user.model.User;
import project.como.domain.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
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
                new IllegalArgumentException("댓글 쓰기 실패: 해당 회원이 존재하지 않습니다." + username));
        Post findPost = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + postId));


        Comment comment = dto.toEntity(findUser, findPost);
        commentRepository.save(comment);
    }

    @Override
    public CommentDto findComment(Long commentId) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 회원이 존재하지 않습니다." + commentId));

        return new CommentDto(findComment);
    }

    @Override
    public List<CommentDto> findComments(Long postId) {
        List<CommentDto> comments = commentRepository.findAllByPostId(postId).stream()
                .map(c -> new CommentDto(c))
                .collect(Collectors.toList());
        return comments;
    }

    @Transactional
    @Override
    public CommentDto updateComment(Long commentId, CommentUpdateDto dto)  {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 회원이 존재하지 않습니다." + commentId));

        findComment.updateBody(dto.getBody());
        CommentDto commentDto = new CommentDto(findComment);
        //시간 변경 로직 추가 필요
        return commentDto;
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId) {

        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 회원이 존재하지 않습니다." + commentId));
        commentRepository.delete(findComment);
    }

}
