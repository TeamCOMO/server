package project.como.global.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.como.domain.post.exception.PostAccessDeniedException;
import project.como.domain.post.exception.PostNotFoundException;
import project.como.global.auth.exception.ComoLoginFailureException;
import project.como.global.common.dto.ErrorResponse;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ComoControllerAdvice {

	@ExceptionHandler(value = BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
		return ResponseEntity.status(UNAUTHORIZED).body(new ErrorResponse(new ComoLoginFailureException()));
	}

	@ExceptionHandler(value = PostNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlePostNotFoundException(PostNotFoundException ex) {
		return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(new PostNotFoundException()));
	}

	@ExceptionHandler(value = PostAccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handlePostAccessDeniedException(PostAccessDeniedException ex) {
		return ResponseEntity.status(FORBIDDEN).body(new ErrorResponse(new PostAccessDeniedException()));
	}
}
