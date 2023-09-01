package project.como.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.como.global.common.exception.ComoException;

@AllArgsConstructor
@Getter
public class ErrorResponse {
	private int errorCode;
	private String errorType;
	private String detail;

	public ErrorResponse(ComoException comoException) {
		this.errorCode = comoException.getErrorCode();
		this.errorType = comoException.getErrorType().toString();
		this.detail = comoException.getDetail();
	}
}
