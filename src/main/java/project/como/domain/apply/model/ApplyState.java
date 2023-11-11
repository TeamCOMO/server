package project.como.domain.apply.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplyState {
    SUBMIT("지원완료"),
    ON_PROGRESS("검토중"),
    ACCEPTED("합격");

    private String state;
}
