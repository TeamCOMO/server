package project.como.domain.comment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import project.como.domain.post.model.Post;
import project.como.domain.user.model.User;
import project.como.global.common.model.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

	@Id
	@Column(name = "comment_id")
	@GeneratedValue //일단 sequence로 설정, h2 사용
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "post_id") // post 연관관계 추가
	private Post post;

	@NotBlank
	private String body;

	public void updateBody (String body){
		this.body = body;
	}

}
