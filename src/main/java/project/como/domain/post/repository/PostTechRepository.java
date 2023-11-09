package project.como.domain.post.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import project.como.domain.post.model.PostTech;

public interface PostTechRepository extends JpaRepository<PostTech, Long> {
    List<PostTech> findAllByPostId(Long postId);

    void deleteAllByPostId(Long postId);
}
