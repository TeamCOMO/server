package project.como.global.auth.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class UnauthorizedException extends ComoException.UnauthorizedException {
	public UnauthorizedException() {
		super(ErrorType.Unauthorized.UNAUTHORIZED_DEFAULT, "인증되지 않은 사용자입니다. 로그인해주세요.");
	}
}
