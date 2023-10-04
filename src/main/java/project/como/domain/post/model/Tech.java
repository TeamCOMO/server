package project.como.domain.post.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tech {

	@GeneratedValue(strategy = IDENTITY)
	@Id @Column(name = "tech_id")
	private Long id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Post post;

	private String stack;
}
