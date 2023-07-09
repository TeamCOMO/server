package project.como.global.auth.repository;

import org.springframework.data.repository.CrudRepository;
import project.como.global.auth.RefreshToken;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, Long> {
	RefreshToken findByRefreshToken(String refreshToken);
}
