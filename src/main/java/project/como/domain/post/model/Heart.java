package project.como.domain.post.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import project.como.domain.post.model.Post;
import project.como.domain.user.model.User;

import static jakarta.persistence.GenerationType.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Heart {

	@GeneratedValue(strategy = IDENTITY)
	@Id @Column(name = "heart_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
}