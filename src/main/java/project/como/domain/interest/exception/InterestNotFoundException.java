package project.como.domain.interest.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class InterestNotFoundException extends ComoException.NotFoundException {
    public InterestNotFoundException(Long interestId) {
        super(ErrorType.NotFound.INTEREST_NOT_FOUND, "ID가 " + interestId + "인 interest를 찾을 수 없습니다.");
    }
}
