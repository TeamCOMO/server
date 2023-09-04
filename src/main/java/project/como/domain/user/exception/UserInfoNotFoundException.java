package project.como.domain.user.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class UserInfoNotFoundException extends ComoException.NotFoundException {

	public UserInfoNotFoundException() {
		super(ErrorType.NotFound.USER_INFO_NOT_FOUND, "사용자의 블로그 또는 Github 정보를 찾을 수 없습니다.");
	}
}
