package project.como.domain.interest.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.como.domain.interest.model.Interest;
import project.como.domain.post.model.Post;
import project.como.domain.user.model.User;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    @Query("select i from Interest i join fetch i.post join fetch i.user where i.user = :user")
    public List<Interest> findAllByUser(@Param("user") User user); // 나중에 컬렉션 Enum(Post) N+1 처리

    boolean existsByUserAndPost(User findUser, Post findPost);

    @Modifying
    @Query("DELETE FROM Interest i WHERE i.post.id = :postId")
    void deleteAllByPostId(@Param("postId")Long postId);
}
