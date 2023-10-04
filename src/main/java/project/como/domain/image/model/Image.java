package project.como.domain.image.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.como.domain.post.model.Post;

import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {

	@GeneratedValue(strategy = IDENTITY)
	@Id @Column(name = "image_id")
	private Long id;

	@ManyToOne
	@JsonIgnore
	private Post post;

	private String url;
}
