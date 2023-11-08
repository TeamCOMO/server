package project.como.domain.post.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.CascadeType.*;
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

//	@OneToMany(mappedBy = "Tech", cascade = REMOVE)
//	private List<PostTech> postList = new ArrayList<>();

	private String stack;

//	public void addPost(PostTech post) {
//		postList.add(post);
//	}
}
