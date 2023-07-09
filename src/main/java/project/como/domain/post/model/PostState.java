package project.como.domain.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostState {
	Active("모집중"),
	Inactive("모집완료");

	private String state;
}
