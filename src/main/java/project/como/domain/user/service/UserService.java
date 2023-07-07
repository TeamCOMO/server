package project.como.domain.user.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.user.dto.MemberSignupRequestDto;
import project.como.domain.user.model.User;
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
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void signUp(MemberSignupRequestDto dto) throws Exception {
		if (userRepository.findByUserId(dto.getUserId()).isPresent())
			throw new Exception("이미 존재하는 아이디입니다.");

		if (!dto.getPassword().equals(dto.getCheckedPassword()))
			throw new Exception("비밀번호가 일치하지 않습니다.");

		User user = userRepository.save(dto.toEntity());
		user.encodePassword(passwordEncoder);

		log.info("회원가입 완료: {}", user);
	}

	@Transactional
	public TokenInfo login(String id, String password) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, password);

		log.info("로그인 시도: {}", authenticationToken);
		log.info("비밀번호 : {}", passwordEncoder.encode(password));

		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		log.info("로그인 성공: {}", authentication);

		TokenInfo tokenInfo = jwtProvider.generateToken(authentication);

		return tokenInfo;
	}
}
