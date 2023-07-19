package project.como.domain.post.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class PostAccessDeniedException extends ComoException.UnauthorizedException {
	public PostAccessDeniedException() {
		super(ErrorType.Unauthorized.UNAUTHORIZED_DEFAULT, "해당 게시물에 대한 권한이 없습니다.");
	}
}
