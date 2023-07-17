package project.como.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.comment.dto.CreateCommentRequest;
import project.como.domain.comment.model.Comment;
import project.como.domain.comment.reposiory.CommentRepository;
import project.como.domain.post.model.Post;
import project.como.domain.user.model.User;
import project.como.domain.user.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImp implements CommentService{

    private final CommentRepository commentRepository;
    //private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional()
    @Override
    public void writeComment(String userId, Long postId, CreateCommentRequest dto) {
        Optional<User> findUser = userRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 회원이 존재하지 않습니다." + userId));
        Optional<Post> findPost = postRepository.fondByPostId(postId).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + postId));


        Comment comment = dto.toEntity(findUser, findPost);
        commentRepository.save();
    }

    @Transactional
    @Override
    public Comment updateComment(Long commentId, String body) throws Exception {
        Optional<Comment> findComment = commentRepository.findById(commentId);
        if(findComment.get() == null){
            throw new Exception("존재하지 않는 댓글입니다.");
        }
        Comment comment = findComment.get();
        comment.updateBody(body);

        return comment;
    }

    @Override
    public List<Comment> findComments() {
        List<Comment> all = commentRepository.findAll();
        return all;
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId) throws Exception {
        Optional<Comment> byId = commentRepository.findById(commentId);
        Optional<Comment> findComment = commentRepository.findById(commentId);
        if(findComment.get() == null){
            throw new Exception("존재하지 않는 댓글입니다.");
        }
        commentRepository.delete(findComment.get());
    }
}
