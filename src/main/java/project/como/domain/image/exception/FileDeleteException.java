package project.como.domain.image.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class FileDeleteException extends ComoException.ServerErrorException {
	public FileDeleteException() {
		super(ErrorType.ServerError.FILE_DELETE_FAIL, "파일 삭제에 실패하였습니다.");
	}
}
