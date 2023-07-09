package project.como.global.auth;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "refresh", timeToLive = 60 * 60 * 24 * 14) // 2 weeks
public class RefreshToken {

	@Id
	private String id;

	private String ip;

	private Collection<? extends GrantedAuthority> authorities;

	@Indexed
	private String refreshToken;
}
