package project.como.domain.user.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class UserNotFoundException extends ComoException.NotFoundException {
	public UserNotFoundException() {
		super(ErrorType.NotFound.USER_NOT_FOUND, "해당 유저를 찾을 수 없습니다");
	}
}
