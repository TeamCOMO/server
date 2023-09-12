package project.como.domain.post.exception;

import project.como.global.common.exception.ComoException;
import project.como.global.common.exception.ErrorType;

public class PostImageUrlNotFoundException extends ComoException.NotFoundException {
	public PostImageUrlNotFoundException(String url) {
		super(ErrorType.NotFound.POST_IMAGE_URL_NOT_FOUND,
				String.format("URL이 '%s'인 이미지를 찾을 수 없습니다.", url));
	}
}
