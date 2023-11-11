package project.como.domain.apply.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType.NotFound;

public class ApplyNotFoundException extends ComoException.NotFoundException {
    public ApplyNotFoundException() {
        super(NotFound.APPLY_NOT_FOUND, "지원 이력이 존재하지 않습니다.");
    }
}
