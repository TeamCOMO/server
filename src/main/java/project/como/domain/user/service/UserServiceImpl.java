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
import project.como.domain.user.dto.request.MemberLoginRequestDto;
import project.como.domain.user.dto.request.MemberSignupRequestDto;
import project.como.domain.user.dto.request.MemberModifyRequestDto;
import project.como.domain.user.dto.response.UserMypageResponseDto;
import project.como.domain.user.exception.UserNotFoundException;
import project.como.domain.user.model.User;
import project.como.domain.user.repository.UserRepository;
import project.como.global.auth.service.JwtProvider;
import project.como.global.auth.model.RefreshToken;
import project.como.global.auth.model.TokenInfo;
import project.como.global.auth.repository.RefreshTokenRedisRepository;
import project.como.global.common.dto.ApiResponse;
import project.como.global.common.model.Helper;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtProvider jwtProvider;
	private final PasswordEncoder passwordEncoder;
	private final ApiResponse response;
	private final RefreshTokenRedisRepository refreshTokenRedisRepository;

	@Transactional
	public void signUp(MemberSignupRequestDto dto) throws Exception {
		if (userRepository.findByUsername(dto.getUsername()).isPresent())
			throw new Exception("이미 존재하는 아이디입니다.");

		if (!dto.getPassword().equals(dto.getCheckedPassword()))
			throw new Exception("비밀번호가 일치하지 않습니다.");

		User user = userRepository.save(dto.toEntity());
		user.encodePassword(passwordEncoder);
	}

	@Transactional
	public String signIn(HttpServletRequest request, MemberLoginRequestDto dto) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

		TokenInfo tokenInfo = jwtProvider.generateToken(authentication);

		refreshTokenRedisRepository.save(RefreshToken.builder()
				.id(authentication.getName())
				.ip(Helper.getIp(request))
				.authorities(authentication.getAuthorities())
				.refreshToken(tokenInfo.getRefreshToken())
				.build());

		return tokenInfo.getAccessToken();
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

	public User getUser(String username) {
		return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
	}

	public boolean checkDuplicate(String username) {
		return userRepository.findByUsername(username).isPresent();
	}

	@Transactional
	public void modify(String username, MemberModifyRequestDto dto) {
		User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

		if (dto.blog_url() != null) user.setBlogUrl(dto.blog_url());
		if (dto.github_url() != null) user.setGithubUrl(dto.github_url());
		if (dto.nickname() != null) user.setNickname(dto.nickname());
		if (dto.password() != null) {
			user.setPassword(dto.password());
			user.encodePassword(passwordEncoder);
		}
	}

	public UserMypageResponseDto myPage(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
		return UserMypageResponseDto.of(user);
	}
}
