package project.como.domain.interest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.como.domain.interest.model.Interest;
import project.como.domain.post.model.Post;
import project.como.domain.user.model.User;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    @Query("select i from Interest i where i.user = :user")
    public List<Interest> findAllbyUser(User user);

    boolean existsByUserAndPost(User findUser, Post findPost);
}
