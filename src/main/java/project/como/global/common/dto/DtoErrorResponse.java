package project.como.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.como.global.common.exception.ComoException;

import java.util.List;

@AllArgsConstructor
@Getter
public class DtoErrorResponse {
	private int errorCode;
	private String errorType;
	private String detail;
	private List<String> errorList;

	public DtoErrorResponse(ComoException comoException, List<String> errorList) {
		this.errorCode = comoException.getErrorCode();
		this.errorType = comoException.getErrorType().toString();
		this.detail = comoException.getDetail();
		this.errorList = errorList;
	}
}
