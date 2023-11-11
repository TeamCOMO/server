package project.como.global.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.como.global.auth.exception.ComoLoginFailureException;
import project.como.global.common.dto.ErrorResponse;

import static org.springframework.http.HttpStatus.*;
import static project.como.global.common.exception.ComoException.*;

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

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponse> unauthorized(ComoException ex) {
		return ResponseEntity.status(UNAUTHORIZED).body(new ErrorResponse(ex));
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ErrorResponse> forbidden(ComoException ex) {
		return ResponseEntity.status(FORBIDDEN).body(new ErrorResponse(ex));
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> notfound(ComoException ex) {
		return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(ex));
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ErrorResponse> conflict(ComoException ex) {
		return ResponseEntity.status(CONFLICT).body(new ErrorResponse(ex));
	}

	@ExceptionHandler(ServerErrorException.class)
	public ResponseEntity<ErrorResponse> serverError(ComoException ex) {
		return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResponse(ex));
	}

	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<ErrorResponse> invalidRequest(ComoException ex) {
		return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(ex));
	}
}
