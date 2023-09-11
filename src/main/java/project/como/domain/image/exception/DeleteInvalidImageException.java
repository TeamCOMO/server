package project.como.domain.image.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class DeleteInvalidImageException extends ComoException.InvalidRequestException {
	public DeleteInvalidImageException() {
		super(ErrorType.BadRequest.DELETE_INVALID_IMAGE, "최소 1개의 이미지를 포함해야 합니다.");
	}
}
