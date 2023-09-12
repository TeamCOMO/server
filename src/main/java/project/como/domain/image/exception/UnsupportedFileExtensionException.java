package project.como.domain.image.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class UnsupportedFileExtensionException extends ComoException.InvalidRequestException {
	public UnsupportedFileExtensionException() {
		super(ErrorType.BadRequest.UNSUPPORTED_FILE_EXTENSION, "지원하지 않는 파일 확장자입니다.");
	}
}
