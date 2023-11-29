package project.como.domain.user.dto.response;

import project.como.domain.user.model.User;

public record UserMypageResponseDto(
        String nickname,
        String email,
        String github_url,
        String blog_url
) {
    public static UserMypageResponseDto of(User user) {
        return new UserMypageResponseDto(
                user.getNickname(),
                user.getEmail(),
                user.getGithubUrl(),
                user.getBlogUrl());
    }
}
