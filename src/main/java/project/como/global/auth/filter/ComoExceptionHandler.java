package project.como.global.auth.filter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import project.como.global.auth.exception.ComoAccessDeniedException;
import project.como.global.auth.exception.UnauthorizedException;
import project.como.global.common.dto.ErrorResponse;

import java.io.IOException;

import static com.google.gson.FieldNamingPolicy.*;
import static jakarta.servlet.http.HttpServletResponse.*;

@Slf4j
@Component
public class ComoExceptionHandler implements AccessDeniedHandler, AuthenticationEntryPoint {
	private final Gson gson = new GsonBuilder().setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();
	private final ErrorResponse accessDenied = new ErrorResponse(new ComoAccessDeniedException());
	private final ErrorResponse unauthorized = new ErrorResponse(new UnauthorizedException());

	private void prepareResponse(
			HttpServletRequest request, HttpServletResponse response, int status
	) throws IOException {
		String token = request.getHeader("Authorization");
		if (token != null) {
			if (token.length() > 20)
				token = token.substring(0, 20) + "...";
			else if (token.isEmpty())
				token = "empty";
		}
		log.info("{}: URI = {} {}, token = {}, error = {}",
				status == SC_FORBIDDEN ? "Forbidden" : "Unauthorized",
				request.getMethod(),
				request.getRequestURI(),
				token == null ? "null" : token,
				request.getAttribute("error-response") == null ? "null" :
						((ErrorResponse) request.getAttribute("error-response")).getDetail());

		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		if (request.getAttribute("error-response") == null) {
			response.getWriter().write(gson.toJson(status == SC_FORBIDDEN ? accessDenied : unauthorized));
		} else {
			response.getWriter().write(gson.toJson(request.getAttribute("error-response")));
		}
		response.setStatus(status);
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		prepareResponse(request, response, SC_FORBIDDEN);
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		prepareResponse(request, response, SC_UNAUTHORIZED);
	}
}
