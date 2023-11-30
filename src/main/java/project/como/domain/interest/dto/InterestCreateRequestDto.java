package project.como.domain.interest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.como.domain.interest.model.Interest;
import project.como.domain.post.model.Post;
import project.como.domain.user.model.User;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterestCreateRequestDto {
    @NotNull
    private Long postId;

    public Interest toEntity(User user, Post post){
        return Interest.builder()
                .user(user)
                .post(post)
                .build();
    }
}