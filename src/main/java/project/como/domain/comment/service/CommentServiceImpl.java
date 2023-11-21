package project.como.domain.comment.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.comment.dto.CommentCreateRequestDto;
import project.como.domain.comment.dto.CommentDetailDto;
import project.como.domain.comment.dto.CommentResponseDto;
import project.como.domain.comment.exception.CommentForbiddenAccessException;
import project.como.domain.comment.exception.CommentLevelExceedException;
import project.como.domain.comment.exception.CommentNotFoundException;
import project.como.domain.comment.model.Comment;
import project.como.domain.comment.repository.CommentRepository;
import project.como.domain.post.exception.PostNotFoundException;
import project.como.domain.post.model.Post;
import project.como.domain.post.repository.PostRepository;
import project.como.domain.user.exception.UserNotFoundException;
import project.como.domain.user.model.User;
import project.como.domain.user.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void writeComment(String username, Long postId, CommentCreateRequestDto dto) {
        User findUser = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        Comment findParent = null;
        if(dto.getParentId() != null) {
            findParent = commentRepository.findById(dto.getParentId()).orElseThrow(() -> new CommentNotFoundException(dto.getParentId()));

            int parentLevel = getParentLevel(findParent);
            if (parentLevel > 3) {
                throw new CommentLevelExceedException();
            }
        }

        Comment comment = Comment.builder()
                .user(findUser)
                .post(findPost)
                .body(dto.getBody())
                .parent(findParent)
                .build();

        commentRepository.save(comment);
    }

    @Override
    public CommentDetailDto findComment(Long commentId) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException(commentId));

        return CommentDetailDto.builder()
                .id(findComment.getId())
                .body(findComment.getBody())
                .build();
    }

    @Override
    public CommentResponseDto findComments(Long postId) {
        if( postRepository.findById(postId).isEmpty()){
            throw new PostNotFoundException();
        }
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        List<CommentDetailDto> commentDetailList = buildCommentTree(comments, null);

        return CommentResponseDto.builder()
                .comments(commentDetailList)
                .build();
    }

    @Transactional
    @Override
    public void updateComment(String username, Long commentId, CommentDetailDto dto) {
        User findUser = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException(commentId));

        if (!checkUpdate(findUser, findComment)) {
            throw new CommentForbiddenAccessException();
        }

        findComment.updateBody(dto.getBody());
    }

    @Transactional
    @Override
    public void deleteComment(String username, Long commentId) {
        User findUser = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException());
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException(commentId));

        if (!checkDelete(findUser, findComment))
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

    public List<CommentDetailDto> buildCommentTree(List<Comment> comments, Comment parent) {

        return comments.stream()
                .filter(comment -> comment.getParent() == parent)
                .map(comment -> CommentDetailDto.builder()
                        .id(comment.getId())
                        .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                        .body(comment.getBody())
                        .nickname(comment.getUser().getNickname())
                        .children(buildCommentTree(comments, comment))
                        .build())
                .collect(Collectors.toList());
    }
    public int getParentLevel(Comment parentComment) {
        int level = 1;
        Comment currentComment = parentComment;

        while (currentComment != null) {
            level++;
            currentComment = currentComment.getParent();
        }

        return level;
    }

}