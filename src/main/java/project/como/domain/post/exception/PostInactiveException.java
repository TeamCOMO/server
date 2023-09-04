package project.como.domain.post.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class PostInactiveException extends ComoException.InvalidRequestException {
	public PostInactiveException() {
		super(ErrorType.BadRequest.INACTIVE_POST, "지원 가능 기간이 아닙니다.");
	}
}
