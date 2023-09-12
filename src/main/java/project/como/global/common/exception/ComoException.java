package project.como.global.common.exception;

import lombok.Getter;

@Getter
public class ComoException extends RuntimeException {
	int errorCode;
	ErrorType errorType;
	String detail;

	public ComoException(ErrorType errorType, String detail) {
		this.errorCode = errorType.getCode();
		this.errorType = errorType;
		this.detail = detail;
	}

	public abstract static class InvalidRequestException extends ComoException {
		public InvalidRequestException(ErrorType.BadRequest errorType, String detail) {
			super(errorType, detail);
		}
	}

	public abstract static class UnauthorizedException extends ComoException {
		public UnauthorizedException(ErrorType.Unauthorized errorType, String detail) {
			super(errorType, detail);
		}
	}

	public abstract static class ForbiddenException extends ComoException {
		public ForbiddenException(ErrorType.Forbidden errorType, String detail) {
			super(errorType, detail);
		}
	}

	public abstract static class NotFoundException extends ComoException {
		public NotFoundException(ErrorType.NotFound errorType, String detail) {
			super(errorType, detail);
		}
	}

	public abstract static class ConflictException extends ComoException {
		public ConflictException(ErrorType.Conflict errorType, String detail) {
			super(errorType, detail);
		}
	}

	public abstract static class ServerErrorException extends ComoException {
		public ServerErrorException(ErrorType errorType, String detail) {
			super(errorType, detail);
		}
	}
}
