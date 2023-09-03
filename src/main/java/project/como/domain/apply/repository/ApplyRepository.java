package project.como.domain.apply.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.como.domain.apply.model.Apply;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {
}
