package project.como.global.common.exception;

public interface ErrorType {
	int getCode();

	enum BadRequest implements ErrorType {
		BAD_REQUEST_DEFAULT(0),
		INVALID_REQUEST_BODY(1),
		INVALID_PARAMETER(2),
		INVALID_JWT_TOKEN(3),
		INVALID_DTO_PARAMETER(4),

		SCRAP_MY_POST(102),
		HEART_MY_POST(103),
		INACTIVE_POST(104),
		NOT_ELIGIBLE_FOR_APPLICATION(105)
		;

		private final int errorCode;

		BadRequest(int errorCode) {
			this.errorCode = errorCode;
		}

		@Override
		public int getCode() {
			return this.errorCode;
		}
	}

	enum Unauthorized implements ErrorType {
		UNAUTHORIZED_DEFAULT(1000),
		LOGIN_FAILED(1001)
		;

		private final int errorCode;

		Unauthorized(int errorCode) {
			this.errorCode = errorCode;
		}

		@Override
		public int getCode() {
			return this.errorCode;
		}
	}

	enum Forbidden implements ErrorType {
		FORBIDDEN_DEFAULT(3000),
		COMMENT_ACCESS_FORBIDDEN(3001),
		ACCESS_DENIED(3100),
		;

		private final int errorCode;

		Forbidden(int errorCode) {
			this.errorCode = errorCode;
		}

		@Override
		public int getCode() {
			return this.errorCode;
		}
	}

	enum NotFound implements ErrorType {
		NOT_FOUND_DEFAULT(4000),
		POST_NOT_FOUND(4001),
		USER_NOT_FOUND(4002),
		USER_INFO_NOT_FOUND(4003),
		COMMENT_NOT_FOUND(4004),

		INTEREST_NOT_FOUND(4005),
		HEART_NOT_FOUND(4006)
		;

		private final int errorCode;

		NotFound(int errorCode) {
			this.errorCode = errorCode;
		}

		@Override
		public int getCode() {
			return this.errorCode;
		}
	}

	enum Conflict implements ErrorType {
		CONFLICT_DEFAULT(9000),
		DUPLICATE_HEART(9100)
		;

		private final int errorCode;

		Conflict(int errorCode) {
			this.errorCode = errorCode;
		}

		@Override
		public int getCode() {
			return this.errorCode;
		}
	}

	enum ServerError implements ErrorType {
		SERVER_ERROR_DEFAULT(5000)
		;

		private final int errorCode;

		ServerError(int errorCode) {
			this.errorCode = errorCode;
		}

		@Override
		public int getCode() {
			return this.errorCode;
		}
	}
}
