package project.como.domain.heart.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class HeartConflictException extends ComoException.ConflictException {

	public HeartConflictException() { super(ErrorType.Conflict.DUPLICATE_HEART, "같은 게시물에 좋아요를 중복할 수 없습니다.");}
}
