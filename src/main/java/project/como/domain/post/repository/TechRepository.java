package project.como.domain.post.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.como.domain.post.model.Tech;

import java.util.List;

@Repository
public interface TechRepository extends JpaRepository<Tech, Long> {

	Optional<Tech> findByStack(String stack);
}
