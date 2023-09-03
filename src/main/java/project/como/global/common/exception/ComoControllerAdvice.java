package project.como.global.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.como.domain.post.exception.*;
import project.como.domain.user.exception.UserInfoNotFoundException;
import project.como.domain.user.exception.UserNotEligibleForApplyException;
import project.como.global.auth.exception.ComoLoginFailureException;
import project.como.global.common.dto.ErrorResponse;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ComoControllerAdvice {

	/*
	Security Exception
	 */
	@ExceptionHandler(value = BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
		return ResponseEntity.status(UNAUTHORIZED).body(new ErrorResponse(new ComoLoginFailureException()));
	}

	/*
	Post Exception
	 */
	@ExceptionHandler(value = PostNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlePostNotFoundException(PostNotFoundException ex) {
		return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(new PostNotFoundException()));
	}

	@ExceptionHandler(value = PostAccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handlePostAccessDeniedException(PostAccessDeniedException ex) {
		return ResponseEntity.status(FORBIDDEN).body(new ErrorResponse(new PostAccessDeniedException()));
	}

	@ExceptionHandler(value = UserNotEligibleForApplyException.class)
	public ResponseEntity<ErrorResponse> handleUserNotEligibleForApplyException(UserNotEligibleForApplyException ex) {
		return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(new UserNotEligibleForApplyException()));
	}

	@ExceptionHandler(value = PostInactiveException.class)
	public ResponseEntity<ErrorResponse> handlePostInactiveException(PostInactiveException ex) {
		return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(new PostInactiveException()));
	}

	@ExceptionHandler(value = HeartConflictException.class)
	public ResponseEntity<ErrorResponse> handleHeartConflictException(HeartConflictException ex) {
		return ResponseEntity.status(CONFLICT).body(new ErrorResponse(new HeartConflictException()));
	}

	@ExceptionHandler(value = HeartNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleHeartNotFoundException(HeartNotFoundException ex) {
		return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(new HeartNotFoundException()));
	}

	/*
	Apply Exception
	 */
	@ExceptionHandler(value = UserInfoNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserInfoNotFoundException(UserInfoNotFoundException ex) {
		return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(new UserInfoNotFoundException()));
	}
}
