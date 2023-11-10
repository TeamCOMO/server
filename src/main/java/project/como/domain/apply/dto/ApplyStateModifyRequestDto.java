package project.como.domain.apply.dto;

import project.como.domain.apply.model.ApplyState;

public record ApplyStateModifyRequestDto(
        String applicantName,
        ApplyState state
) {
}
