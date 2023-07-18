package project.como.domain.post.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class PostNotFoundException extends ComoException.NotFoundException {

	public PostNotFoundException(Long postId) {
		super(ErrorType.NotFound.POST_NOT_FOUND, "ID가 " + postId + "인 게시글을 찾을 수 없습니다.");
	}
}
