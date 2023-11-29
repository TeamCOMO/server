package project.como.domain.user.dto.response;

public record UserPagingResponseDto(
        String nickname,
        String email,
        String github_url,
        String blog_url
) {
}
