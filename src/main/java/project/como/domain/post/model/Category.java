package project.como.domain.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
	Study("스터디"),
	Project("프로젝트");

	private String category;
}
