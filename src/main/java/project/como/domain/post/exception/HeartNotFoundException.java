package project.como.domain.post.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class HeartNotFoundException extends ComoException.NotFoundException {
	public HeartNotFoundException() { super(ErrorType.NotFound.HEART_NOT_FOUND, "하트를 누르지 않아 취소할 수 없습니다."); }
}
