package project.como.domain.post.model;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostTech {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tech_id")
    private Tech tech;

//    public PostTech(Post post, Tech tech) {
//        setPost(post);
//        setTech(tech);
//    }
//
//    private void setPost(Post post) {
//        this.post = post;
//        tech.addPost(this);
//    }
//
//    private void setTech(Tech tech) {
//        this.tech = tech;
//        tech.addPost(this);
//    }
}
