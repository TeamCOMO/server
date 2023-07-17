//package project.como.global.auth.filter;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//import project.como.global.auth.repository.CookieAuthorizationRequestRepository;
//import project.como.global.auth.service.JwtProvider;
//import project.como.global.common.model.CookieUtils;
//
//import java.io.IOException;
//import java.util.Optional;
//
//import static project.como.global.auth.repository.CookieAuthorizationRequestRepository.*;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class OAuth2AuthenticationSuccessHandler extends
//		SimpleUrlAuthenticationSuccessHandler {
//
//	@Value("${oauth.authorizedRedirectUri}")
//	private String redirectUri;
//	private final JwtProvider jwtProvider;
//	private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
//
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//		String targetUrl = determineTargetUrl(request, response, authentication);
//
//		if (response.isCommitted()) {
//			log.info("Response has already been committed");
//			return;
//		}
//		clearAuthenticationAttributes(request);
//		getRedirectStrategy().sendRedirect(request, response, targetUrl);
//	}
//
//	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//		Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
//				.map(Cookie::getValue);
//		String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
//
////		String token = jwtProvider.generateToken(authentication);
//		targetUrl = targetUrl + "?token=" + token;
//
//		return targetUrl;
//	}
//}
