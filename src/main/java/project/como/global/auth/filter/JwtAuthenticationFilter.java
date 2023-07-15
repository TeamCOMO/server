package project.como.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import project.como.global.auth.service.JwtProvider;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final JwtProvider jwtProvider;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String token = jwtProvider.resolveToken((HttpServletRequest) request);

		if (token != null && jwtProvider.validateToken(token)) {
			/*
			 * 토큰 재발급 요청 시 refresh token을 요청 header에 넣어 Authorization 키로 받는다면,
			 * refresh token은 내부적으로 권한 정보가 없기 때문에 getAuthentication 부분에서 Exception이 발생하므로
			 * 한번 필터링을 해줘야 한다.
			 */
			if (!((HttpServletRequest) request).getRequestURI().equals("/user/reissue")) {
				Authentication authentication = jwtProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(request, response);
	}
}
