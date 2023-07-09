package project.como.domain.interest.model;

import jakarta.persistence.*;
import project.como.domain.post.model.Post;
import project.como.domain.user.model.User;

import static jakarta.persistence.GenerationType.*;

@Entity
public class Interest {

	@GeneratedValue(strategy = IDENTITY)
	@Id @Column(name = "interest_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;
}
