package project.como.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.como.domain.user.model.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findById(Long userId);

	Optional<User> findByUsername(String username);
}
