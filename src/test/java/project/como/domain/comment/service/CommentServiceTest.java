package project.como.domain.comment.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.comment.dto.CommentCreateRequestDto;
import project.como.domain.comment.dto.CommentCreateResponseDto;
import project.como.domain.comment.dto.CommentDetailDto;
import project.como.domain.comment.exception.CommentForbiddenAccessException;
import project.como.domain.comment.exception.CommentLevelExceedException;
import project.como.domain.comment.model.Comment;
import project.como.domain.comment.repository.CommentRepository;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.Post;
import project.como.domain.post.model.PostState;
import project.como.domain.post.repository.PostRepository;
import project.como.domain.user.model.User;
import project.como.domain.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class CommentServiceTest {

    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    static Long postId;
    static Post post;
    static User user;
    @BeforeAll
    public void init(){
        user = userRepository.save(User.builder()
                .username("testName")
                .password("test123456@")
                .email("test9999@naver.com")
                .nickname("test8888")
                .build());

        post = postRepository.save(Post.builder() // User 배제, Post 간단하게만 생성
                .title("test 게시물 제목")
                .body("test 게시물 본문")
                .user(user)
                .category(Category.Study)
                .state(PostState.Active)
                .build());
        postId = post.getId();
    }
    @AfterAll
    public void close(){
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Transactional
    @Test
    @DisplayName("특정 게시물에 댓글 작성하기")
    void createCommentByPostId(){
        //given
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.get();
        List<User> all = userRepository.findAll();
        User user = all.get(0);
        String username = user.getUsername();

        Comment comment1 = createComment(post, user, "댓글1");
        Comment comment2 = createComment(post, user, "댓글2");
        Comment comment3 = createComment(post, user, "댓글3");
        commentRepository.saveAll(List.of(comment1,comment2, comment3));

        CommentCreateRequestDto request = CommentCreateRequestDto.builder()
                .parentId(null)
                .body("댓글4")
                .build();

        //when
        CommentCreateResponseDto response = commentService.create(username, postId, request);

        //then
        assertThat(response.getParentId()).isNull();

        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(4)
                .extracting("body")
                .containsExactlyInAnyOrderElementsOf(
                       List.of("댓글1", "댓글2", "댓글3", "댓글4")
                );

    }

    @Transactional
    @Test
    @DisplayName("특정 게시물에 대댓글 작성하기")
    void createChildCommentByPostId(){
        //given
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.get();
        List<User> all = userRepository.findAll();
        User user = all.get(0);
        String username = user.getUsername();

        Comment comment1 = createComment(post, user, "댓글1");
        Comment comment2 = createComment(post, user, "댓글2");
        Comment comment3 = createComment(post, user, "댓글3");
        commentRepository.saveAll(List.of(comment1,comment2, comment3));

        Optional<Comment> comment = commentRepository.findById(comment2.getId());
        Long id = comment.get().getId();
        CommentCreateRequestDto request = CommentCreateRequestDto.builder()
                .parentId(id)
                .body("대댓글1")
                .build();

        //when
        CommentCreateResponseDto response = commentService.create(username, postId, request);

        //then
        assertThat(response.getParentId()).isEqualTo(comment2.getId());

        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(4)
                .extracting("body")
                .containsExactlyInAnyOrderElementsOf(
                        List.of("댓글1", "댓글2", "댓글3", "대댓글1")
                );
    }

    @Transactional
    @Test
    @DisplayName("대댓글 구조는 최대 3입니다.")
    void commentDepthMaximumUp3(){
        //given
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.get();
        List<User> all = userRepository.findAll();
        User user = all.get(0);
        String username = user.getUsername();

        Comment comment1 = createComment(post, user, "댓글1");
        Comment comment2 = createComment(post, user, "댓글2");
        Comment comment3 = createChildComment(post, user, "대댓글1", comment1);
        Comment comment4 = createChildComment(post, user, "대대댓글1", comment3);
        commentRepository.saveAll(List.of(comment1,comment2,comment3,comment4));

        Optional<Comment> findComment = commentRepository.findById(comment4.getId());
        Comment comment = findComment.get();

        CommentCreateRequestDto request = CommentCreateRequestDto.builder()
                .parentId(comment.getId())
                .body("대대대댓글1")
                .build();

        //when, then
        assertThatThrownBy(() -> commentService.create(username, postId, request))
                .isInstanceOf(CommentLevelExceedException.class);
                //.hasMessage("대댓글 깊이는 최대 3입니다. 해당 댓글에 대댓글을 작성할 수 없습니다.");

    }

    @Transactional
    @Test
    @DisplayName("댓글 단건 삭제하기")
    void deleteComment(){
        //given
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.get();
        List<User> all = userRepository.findAll();
        User user = all.get(0);

        Comment comment1 = createComment(post, user, "댓글1");
        Comment comment2 = createComment(post, user, "댓글2");
        Comment comment3 = createChildComment(post, user, "대댓글1", comment1);
        commentRepository.saveAll(List.of(comment1,comment2,comment3));

        //when
        commentService.deleteById(user.getUsername(), comment2.getId());

        //then
        List<Comment> result = commentRepository.findAll();
        assertThat(result).hasSize(2)
                .extracting("body")
                .contains("댓글1", "대댓글1");
    }

    @Transactional
    @Test
    @DisplayName("댓글 삭제 시, 댓글에 대한 대댓글도 삭제된다.")
    void deleteCommentWithReplies(){
        //given
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.get();
        List<User> all = userRepository.findAll();
        User user = all.get(0);

        Comment comment1 = createComment(post, user, "댓글1");
        Comment comment2 = createComment(post, user, "댓글2");
        Comment comment3 = createComment(post, user, "댓글3");
        Comment comment4 = createChildComment(post, user, "대댓글1", comment1);
        commentRepository.saveAll(List.of(comment1,comment2,comment3,comment4));

        //when
        commentService.deleteById(user.getUsername(), comment1.getId());

        //then
        List<Comment> result = commentRepository.findAll();
        assertThat(result).hasSize(2)
                .extracting("body")
                .contains("댓글2", "댓글3");

    }
    @Transactional
    @Test
    @DisplayName("게시물 작성자 또는 댓글 작성자가 아닌 사용자가 댓글을 삭제하는 경우에 예외가 발생한다.")
    void deleteCommentByOther(){
        // given
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.get();
        List<User> all = userRepository.findAll();
        User user = all.get(0);
        User other = createUser("other", "other@naver.com", "qwer1234!", "다른사용자");
        User savedOther = userRepository.save(other);

        Comment comment1 = createComment(post, user, "댓글1");
        Comment comment2 = createComment(post, user, "댓글2");
        commentRepository.saveAll(List.of(comment1,comment2));

        //when //then
        assertThatThrownBy(() -> commentService.deleteById(savedOther.getUsername(), comment2.getId()))
                .isInstanceOf(CommentForbiddenAccessException.class);
    }

    @Transactional
    @Test
    @DisplayName("게시물 작성자는 다른 사용자의 댓글을 삭제할 수 있다.")
    void deleteCommentByPostWriter(){
        // given
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.get();
        List<User> all = userRepository.findAll();
        User user = all.get(0);

        Comment comment1 = createComment(post, user, "댓글1");
        Comment comment2 = createComment(post, user, "댓글2");
        commentRepository.saveAll(List.of(comment1,comment2));

        //when
        commentService.deleteById(user.getUsername(), comment2.getId());
        // then
        List<Comment> result = commentRepository.findAll();
        assertThat(result).hasSize(1)
                .extracting("body")
                .contains("댓글1");
    }



    @Transactional
    @Test
    @DisplayName("댓글 단건 수정하기")
    void modifyComment(){
        //given
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.get();
        List<User> users = userRepository.findAll();
        User user = users.get(0);
        String username = user.getUsername();

        Comment comment1 = createComment(post, user, "댓글1");
        Comment comment2 = createComment(post, user, "댓글2");
        Comment comment3 = createChildComment(post, user, "대댓글1", comment1);
        commentRepository.saveAll(List.of(comment1,comment2,comment3));

        CommentDetailDto dto = CommentDetailDto.builder()
                .body("수정")
                .build();
        //when
        commentService.modifyById(username, comment2.getId(), dto);

        //then
        Comment result = commentRepository.findById(comment2.getId()).get();
        List<Comment> all = commentRepository.findAll();
        assertThat(all).hasSize(3)
                .extracting("body")
                .contains("댓글1", "수정", "대댓글1");
        assertThat(result).extracting("body")
                .isEqualTo("수정");

    }

    @Transactional
    @Test
    @DisplayName("다른 사용자가 작성한 댓글 수정을 하는 경우에 예외가 발생한다.")
    void modifyCommentByOther(){
        //given
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.get();
        List<User> users = userRepository.findAll();
        User user = users.get(0);

        User other = createUser("other", "other@naver.com", "qwer1234!", "다른사용자");
        User savedOther = userRepository.save(other);

        Comment comment1 = createComment(post, user, "댓글1");
        Comment comment2 = createComment(post, user, "댓글2");
        commentRepository.saveAll(List.of(comment1,comment2));

        CommentDetailDto dto = CommentDetailDto.builder()
                .body("수정")
                .build();
        //when //then
        assertThatThrownBy(() -> commentService.modifyById(savedOther.getUsername(), comment2.getId(), dto))
                .isInstanceOf(CommentForbiddenAccessException.class);
                //.hasMessage("해당 사용자는 댓글 수정 또는 삭제 불가합니다.");
    }

    private static User createUser(String username, String email, String password, String nickname) {
        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }


    private static Comment createComment(Post post, User user, String body) {
        return Comment.builder()
                .user(user)
                .post(post)
                .body(body)
                .build();
    }
    private static Comment createChildComment(Post post, User user, String body, Comment parent) {
        Comment childComment = Comment.builder()
                .user(user)
                .post(post)
                .parent(parent)
                .body(body)
                .build();
        parent.addChild(childComment);
        return childComment;
    }
}
