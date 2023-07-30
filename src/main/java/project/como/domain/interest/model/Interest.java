package project.como.domain.interest.model;

import jakarta.persistence.*;
import lombok.*;
import project.como.domain.post.model.Post;
import project.como.domain.user.model.User;

import static jakarta.persistence.GenerationType.*;

@Entity
@AllArgsConstructor
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

	//단방향이므로 연관관계 메서드 필요 x
}
