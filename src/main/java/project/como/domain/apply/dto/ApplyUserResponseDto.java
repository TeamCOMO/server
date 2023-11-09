package project.como.domain.apply.dto;

import java.util.List;

public record ApplyUserResponseDto(
        String username,
        String email,
        List<String> portfolio
) {
    public static ApplyUserResponseDto of(String username, String email, List<String> portfolio) {
        return new ApplyUserResponseDto(
                username,
                email,
                portfolio
        );
    }
}
