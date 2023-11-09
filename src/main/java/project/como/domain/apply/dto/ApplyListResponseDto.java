package project.como.domain.apply.dto;

import java.util.List;

public record ApplyListResponseDto(
        List<ApplyUserResponseDto> applies
) {
    public static ApplyListResponseDto of(List<ApplyUserResponseDto> applies) {
        return new ApplyListResponseDto(applies);
    }
}
