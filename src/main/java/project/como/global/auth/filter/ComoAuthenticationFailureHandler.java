package project.como.global.auth.filter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import project.como.global.auth.exception.ComoAccessDeniedException;
import project.como.global.auth.exception.UnauthorizedException;
import project.como.global.common.dto.ErrorResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

public class ComoAuthenticationFailureHandler implements AuthenticationFailureHandler {
	private final Gson gson = new GsonBuilder().setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();
	private final ErrorResponse accessDenied = new ErrorResponse(new ComoAccessDeniedException());
	private final ErrorResponse unauthorized = new ErrorResponse(new UnauthorizedException());

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		String errorMessage = null;

		if (exception instanceof BadCredentialsException || exception instanceof InternalAuthenticationServiceException)
			errorMessage = "아이디나 비밀번호가 맞지 않습니다. 다시 확인하여 주십시오.";
		else if (exception instanceof DisabledException)
			errorMessage = "계정이 비활성화 되었습니다. 관리자에게 문의하세요.";
		else if (exception instanceof CredentialsExpiredException)
			errorMessage = "비밀번호 유효기간이 만료되었습니다. 관리자에게 문의하세요.";
		else
			errorMessage = "알수 없는 이유로 로그인에 실패하였습니다. 다시 시도해주세요.";
		Map<String, Object> map = new LinkedHashMap<>();

		map.put("error_code", 1001);
		map.put("error_type", exception);
		map.put("detail", errorMessage);

		response.getWriter().write(gson.toJson(map));
	}
}
