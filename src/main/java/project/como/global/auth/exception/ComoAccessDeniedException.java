package project.como.global.auth.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class ComoAccessDeniedException extends ComoException {
	public ComoAccessDeniedException() {
		super(ErrorType.Forbidden.FORBIDDEN_DEFAULT, "접근 권한이 없습니다.");
	}
}
