package project.como.domain.image.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class FileUploadException extends ComoException.ServerErrorException {
	public FileUploadException() {
		super(ErrorType.ServerError.FILE_UPLOAD_FAIL, "파일 업로드에 실패하였습니다.");
	}
}
