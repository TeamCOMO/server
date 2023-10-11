package project.como.domain.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.como.domain.image.model.Image;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findAllByPostId(Long postId);

	void deleteAllByUrlIn(List<String> urls);
}
