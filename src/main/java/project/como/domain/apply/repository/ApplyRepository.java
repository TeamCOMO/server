package project.como.domain.apply.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.como.domain.apply.model.Apply;
import project.como.domain.post.model.Post;
import project.como.domain.user.model.User;

import java.util.Optional;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {
	Optional<Apply> findApplyByUserAndPost(User user, Post post);

	List<Apply> findAllByPost(Post post);
}
