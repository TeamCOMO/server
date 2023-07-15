package project.como.domain.post.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import project.como.domain.user.model.User;
import project.como.global.common.model.BaseTimeEntity;

import java.util.List;

import static jakarta.persistence.GenerationType.*;

@Entity
public class Post extends BaseTimeEntity {

	@GeneratedValue(strategy = IDENTITY)
	@Id @Column(name = "post_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PostState state;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Category category;

	@Column(nullable = false)
	@ElementCollection
	private List<Stack> stacks;

	@NotBlank
	private String title;

	@NotBlank
	private String body;

	private Long readCount;
}
