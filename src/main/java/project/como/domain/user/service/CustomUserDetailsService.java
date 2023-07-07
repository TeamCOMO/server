package project.como.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.como.domain.user.model.Role;
import project.como.domain.user.model.User;
import project.como.domain.user.repository.UserRepository;
import project.como.global.auth.JwtProvider;
import project.como.global.auth.repository.RefreshTokenRedisRepository;
import project.como.global.common.dto.ApiResponse;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final RefreshTokenRedisRepository refreshTokenRedisRepository;
	private final JwtProvider jwtProvider;
	private final ApiResponse response;


	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

		User user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		grantedAuthorities.add(new SimpleGrantedAuthority(Role.USER.getDescription()));
		if (userId.equals("smc9919")) {
			grantedAuthorities.add(new SimpleGrantedAuthority(Role.ADMIN.getDescription()));
		}

		return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getPassword(), grantedAuthorities);
	}

	private UserDetails createUserDetails(User user) {
		return User.builder()
				.userId(user.getUserId())
				.password(passwordEncoder.encode(user.getPassword()))
				.roles(user.getRoles())
				.build();
	}
}
