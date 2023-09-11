package project.como.domain.comment.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class CommentLevelExceedException extends ComoException.ForbiddenException {
    public CommentLevelExceedException() {
        super(ErrorType.Forbidden.COMMENT_LEVEL_FORBIDDEN, "대댓글 깊이는 최대 3입니다. 해당 댓글에 대댓글을 작성할 수 없습니다.");
    }
}
