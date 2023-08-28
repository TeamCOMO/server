package project.como.domain.post.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import project.como.domain.comment.model.Comment;
import project.como.domain.user.model.User;
import project.como.global.common.model.BaseTimeEntity;

import java.util.Collection;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
	@ElementCollection(fetch = FetchType.EAGER)
	private List<Tech> techs;

	@NotBlank
	private String title;

	@NotBlank
	private String body;

	private Long readCount;

	private Long heartCount;

	public void modifyTitle(String title) {
		this.title = title;
	}

	public void modifyBody(String body) {
		this.body = body;
	}

	public void modifyTechs(List<Tech> techs) {
		this.techs = techs;
	}

	public void modifyCategory(Category category) {
		this.category = category;
	}

	public void modifyState(PostState state) {
		this.state = state;
	}

	public void countRead() {
		++this.readCount;
	}

	public void countHeart() { ++this.heartCount; }
	public void discountHeart() { --this.heartCount; }

	@OneToMany(mappedBy = "post")
	private Collection<Comment> comment;

	public Collection<Comment> getComment() {
		return comment;
	}

	public void setComment(Collection<Comment> comment) {
		this.comment = comment;
	}
}
