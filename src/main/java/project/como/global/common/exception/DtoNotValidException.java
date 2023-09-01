package project.como.global.common.exception;

public class DtoNotValidException extends ComoException.InvalidRequestException {
	public DtoNotValidException() {
		super(ErrorType.BadRequest.INVALID_DTO_PARAMETER, "입력한 정보를 다시 확인해주세요.");
	}
}
