package project.como.domain.comment.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import project.como.domain.comment.model.Comment;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.Post;
import project.como.domain.post.model.PostState;
import project.como.domain.post.repository.PostRepository;
import project.como.domain.user.model.User;
import project.como.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    static Post post1;
    static Post post2;
    static User user;
    @BeforeEach
    void init(){
         post1 = postRepository.save(Post.builder()
                .title("test용 제목1")
                .body("test용 본문1")
                .state(PostState.Active)
                .category(Category.Study)
                .images(new ArrayList<>())
                .techList(new ArrayList<>())
                .build());
         post2 = postRepository.save(Post.builder()
                .title("test용 제목2")
                .body("test용 본문2")
                .state(PostState.Active)
                .category(Category.Study)
                .images(new ArrayList<>())
                .techList(new ArrayList<>())
                .build());

         user = userRepository.save(User.builder()
                .username("testName")
                .password("test123456@")
                .email("test9999@naver.com")
                .nickname("test8888")
                .build());

    }
    @AfterEach
    void close(){
        postRepository.delete(post1); // 만약 운영 DB를 사용한다면 deletAll을 하면 안됨?
        postRepository.delete(post2);
        userRepository.delete(user);
    }
    @Test
    @DisplayName("해당 게시물에 작성된 댓글들만 가져온다.")
    void findAllByPostId(){
        //given
        List<Post> postAll = postRepository.findAll();
        Post post1 = postAll.get(0);
        Post post2 = postAll.get(1);
        User user = userRepository.findAll().get(0);

        Comment comment1 = createComment(post1, user, "댓글1");
        Comment comment2 = createComment(post1, user, "댓글2");
        Comment comment3 = createComment(post2, user, "댓글3");
        Comment comment4 = createComment(post2, user, "댓글4");
        commentRepository.saveAll(List.of(comment1,comment2, comment3, comment4));

        //when
        List<Comment> result = commentRepository.findAllByPostId(post2.getId());
        //then
        assertThat(result).hasSize(2)
                .extracting("body")
                .containsExactlyInAnyOrder("댓글3", "댓글4");

    }
    @Test
    @DisplayName("해당 게시물에 작성된 모든 댓글을 삭제한다.")
    void deleteAllByPostId(){
        //given
        List<Post> postAll = postRepository.findAll();
        Post post1 = postAll.get(0);
        Post post2 = postAll.get(1);
        User user = userRepository.findAll().get(0);

        Comment comment1 = createComment(post1, user, "댓글1");
        Comment comment2 = createComment(post1, user, "댓글2");
        Comment comment3 = createComment(post2, user, "댓글3");
        Comment comment4 = createComment(post2, user, "댓글4");
        Comment comment5 = createComment(post2, user, "댓글5");
        commentRepository.saveAll(List.of(comment1,comment2, comment3, comment4,comment5));

        //when
        commentRepository.deleteAllByPostId(post1.getId());
        //then
        List<Comment> result = commentRepository.findAll();
        assertThat(result).hasSize(3)
                .extracting("body")
                .containsExactlyInAnyOrder("댓글3", "댓글4","댓글5");
    }
    private static Comment createComment(Post post, User user, String body) {
        return Comment.builder()
                .user(user)
                .post(post)
                .body(body)
                .build();
    }
}