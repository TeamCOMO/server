package project.como.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.como.domain.interest.model.Interest;
import project.como.domain.post.dto.PostsResponseDto;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.Post;
import project.como.domain.post.model.PostState;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
	Page<Post> findAllByOrderByCreatedDateDesc(Pageable pageable);
	Page<Post> findAllByCategoryOrderByCreatedDate(Category category, Pageable pageable);
	Page<Post> findAllByCategoryAndStateOrderByCreatedDate(Category category, PostState state, Pageable pageable);

	@Query("select p from Interest i join i.post p where i in :interests") // 사용 가능하지만 N+1 문제가 발생함.
	Page<Post> findInterestPosts(@Param("interests") List<Interest> interestList, Pageable pageable);

}

