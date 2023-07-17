package project.como.domain.user.service;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.user.dto.MemberLoginRequestDto;
import project.como.domain.user.dto.MemberSignupRequestDto;
import project.como.domain.user.model.User;
import project.como.domain.user.repository.UserRepository;
import project.como.global.auth.JwtProvider;
import project.como.global.auth.RefreshToken;
import project.como.global.auth.TokenInfo;
import project.como.global.auth.repository.RefreshTokenRedisRepository;
import project.como.global.common.dto.ApiResponse;
import project.como.global.common.model.Helper;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtProvider jwtProvider;
	private final PasswordEncoder passwordEncoder;
	private final ApiResponse response;
	private final RefreshTokenRedisRepository refreshTokenRedisRepository;

	@Transactional
	public ResponseEntity<?> signUp(MemberSignupRequestDto dto) throws Exception {
		if (userRepository.findByUserId(dto.getUserId()).isPresent())
			throw new Exception("이미 존재하는 아이디입니다.");

		if (!dto.getPassword().equals(dto.getCheckedPassword()))
			throw new Exception("비밀번호가 일치하지 않습니다.");

		User user = userRepository.save(dto.toEntity());
		user.encodePassword(passwordEncoder);

		return response.success("회원가입이 완료되었습니다.");
	}


	@Transactional
	public ResponseEntity<?> signIn(HttpServletRequest request, MemberLoginRequestDto dto) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getUserId(), dto.getPassword());

		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

		TokenInfo tokenInfo = jwtProvider.generateToken(authentication);

		refreshTokenRedisRepository.save(RefreshToken.builder()
				.id(authentication.getName())
				.ip(Helper.getIp(request))
				.authorities(authentication.getAuthorities())
				.refreshToken(tokenInfo.getRefreshToken())
				.build());

		return response.success(tokenInfo);
	}

	@Transactional
	public ResponseEntity<?> reissue(HttpServletRequest request) {
		String token = jwtProvider.resolveToken(request);

		if (token != null && jwtProvider.validateToken(token)) {
			if (jwtProvider.isRefreshToken(token)) {
				RefreshToken refreshToken = refreshTokenRedisRepository.findByRefreshToken(token);
				if (refreshToken != null) {
					String currentIp = Helper.getIp(request);
					if (refreshToken.getIp().equals(currentIp)) {
						TokenInfo tokenInfo = jwtProvider.generateToken(refreshToken.getId(), refreshToken.getAuthorities());

						refreshTokenRedisRepository.save(RefreshToken.builder()
								.id(refreshToken.getId())
								.ip(currentIp)
								.authorities(refreshToken.getAuthorities())
								.refreshToken(tokenInfo.getRefreshToken())
								.build());

						return response.success(tokenInfo);
					}
				}
			}
		}
		return response.fail("토큰 갱신에 실패했습니다.");
	}
}
