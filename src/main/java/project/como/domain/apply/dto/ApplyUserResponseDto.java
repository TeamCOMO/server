package project.como.domain.apply.dto;

import java.util.List;
import project.como.domain.apply.model.ApplyState;

public record ApplyUserResponseDto(
        String username,
        String email,
        List<String> portfolio,
        ApplyState state
) {
    public static ApplyUserResponseDto of(String username, String email, List<String> portfolio, ApplyState state) {
        return new ApplyUserResponseDto(
                username,
                email,
                portfolio,
                state
        );
    }
}
