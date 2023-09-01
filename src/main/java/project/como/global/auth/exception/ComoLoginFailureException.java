package project.como.global.auth.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class ComoLoginFailureException extends ComoException {
	public ComoLoginFailureException() {
		super(ErrorType.Unauthorized.LOGIN_FAILED, "로그인에 실패하였습니다. 다시 시도해주세요.");
	}
}
