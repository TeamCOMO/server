package project.como.domain.comment.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class CommentForbiddenAccessException extends ComoException.ForbiddenException {
    public CommentForbiddenAccessException() {
        super(ErrorType.Forbidden.COMMENT_ACCESS_FORBIDDEN, "해당 사용자는 댓글 수정 또는 삭제 불가합니다.");
    }

}
