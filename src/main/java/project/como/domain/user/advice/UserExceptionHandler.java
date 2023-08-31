package project.como.domain.user.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.como.domain.user.exception.UserNotFoundException;
import project.como.global.common.dto.NotValidExceptionResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class UserExceptionHandler {

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Object> userNotFoundException(BadCredentialsException ex) {
		return new ResponseEntity<>(
				NotValidExceptionResponse.builder()
						.timestamp(LocalDateTime.now())
						.status(HttpStatus.BAD_REQUEST.value())
						.title("로그인에 실패하였습니다.")
						.message(ex.getClass().getName())
						.err(null)
						.build(), HttpStatus.BAD_REQUEST
		);
	}
}
