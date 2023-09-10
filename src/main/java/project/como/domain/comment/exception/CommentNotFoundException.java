package project.como.domain.comment.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class CommentNotFoundException  extends ComoException.NotFoundException {

	public CommentNotFoundException() {
		super(ErrorType.NotFound.COMMENT_NOT_FOUND, "해당 게시글을 찾을 수 없습니다.");
	}

	public CommentNotFoundException(Long commentId) {
		super(ErrorType.NotFound.COMMENT_NOT_FOUND, "ID가 " + commentId + "인 게시글을 찾을 수 없습니다.");
	}
}
