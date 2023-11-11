package project.como.domain.apply.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType.BadRequest;

public class DuplicatedApplyException extends ComoException.InvalidRequestException {
    public DuplicatedApplyException() {
        super(BadRequest.DUPLICATED_APPLY, "같은 공고에 한 번만 지원할 수 있습니다.");
    }
}
