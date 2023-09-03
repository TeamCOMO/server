package project.como.domain.user.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class UserNotEligibleForApplyException extends ComoException.InvalidRequestException {

	public UserNotEligibleForApplyException() {
		super(ErrorType.BadRequest.NOT_ELIGIBLE_FOR_APPLICATION, "해당 공고에 지원할 수 없습니다.");
	}
}
