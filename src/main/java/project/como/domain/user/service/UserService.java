package project.como.domain.user.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.user.repository.UserRepository;
import project.como.global.auth.JwtProvider;
import project.como.global.auth.TokenInfo;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtProvider jwtProvider;

	@Transactional
	public TokenInfo login(String id, String password) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, password);
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		TokenInfo tokenInfo = jwtProvider.generateToken(authentication);

		return tokenInfo;
	}
}
