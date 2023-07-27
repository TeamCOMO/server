package project.como.domain.interest.dto;

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

    private Long postId;
    //private String body;

    public Interest toEntity(User user, Post post){
        return Interest.builder()
                .user(user)
                .post(post)
                .build();
    }
}
/**
 * 필드가 가진 값
 */
