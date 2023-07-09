package project.como.domain.comment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import project.como.domain.user.model.User;
import project.como.global.common.model.BaseTimeEntity;

@Entity
public class Comment extends BaseTimeEntity {

	@Id @Column(name = "comment_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@NotBlank
	private String body;
}
