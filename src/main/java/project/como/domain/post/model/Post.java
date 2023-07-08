package project.como.domain.post.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import project.como.domain.user.model.User;

@Entity
public class Post {

	@Id @Column(name = "post_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@NotBlank
	private String title;

	@NotBlank
	private String body;

	private Long readCount;

	private String status;
}
