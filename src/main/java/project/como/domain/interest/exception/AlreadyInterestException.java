package project.como.domain.interest.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class AlreadyInterestException extends ComoException.ConflictException {
    public AlreadyInterestException(Long UserId, Long InterstId) {
        super(ErrorType.Conflict.DUPLICATE_HEART,
                "ID가 " + UserId + "인 사용자는 이미 ID가 " + InterstId + "인 게시물을 관심 등록했습니다.");
    }
}
