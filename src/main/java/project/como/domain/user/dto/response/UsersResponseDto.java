package project.como.domain.user.dto.response;

import java.util.List;
import org.springframework.data.domain.Page;
import project.como.domain.apply.model.Apply;
import project.como.domain.post.dto.PostPagingResponseDto;
import project.como.domain.user.model.User;

public record UsersResponseDto(
        int totalPages,
        long totalElements,
        int currentPage,
        List<UserPagingResponseDto> users
) {
    public static UsersResponseDto of(Page<Apply> applies) {
        return new UsersResponseDto(
                applies.getTotalPages(),
                applies.getTotalElements(),
                applies.getNumber(),
                applies.stream().map(a -> {
                    User user = a.getUser();
                    return new UserPagingResponseDto(
                            user.getNickname(),
                            user.getEmail(),
                            user.getGithubUrl(),
                            user.getBlogUrl()
                    );
                }).toList()
        );
    }
}
