package project.como.domain.apply.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.como.domain.post.model.Post;
import project.como.domain.user.model.User;

import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Apply {

	@GeneratedValue(strategy = IDENTITY)
	@Id @Column(name = "apply_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Enumerated(EnumType.STRING)
	private ApplyState state;

	public void modifyState(ApplyState state) {
		this.state = state;
	}
}
