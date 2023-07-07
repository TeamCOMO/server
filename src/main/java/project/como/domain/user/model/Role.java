package project.como.domain.user.model;

import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
	USER("유저"),
	ADMIN("어드민");

	private final String description;
}
