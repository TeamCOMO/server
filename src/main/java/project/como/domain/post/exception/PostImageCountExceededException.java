package project.como.domain.post.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class PostImageCountExceededException extends ComoException.ServerErrorException {
	public PostImageCountExceededException() {
		super(ErrorType.ServerError.EXCEEDED_TOTAL_FILE_COUNT, "파일 최대 개수인 5개를 초과했습니다.");
	}
}
