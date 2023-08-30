package project.como.domain.post.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 50045652L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final project.como.global.common.model.QBaseTimeEntity _super = new project.como.global.common.model.QBaseTimeEntity(this);

    public final StringPath body = createString("body");

    public final EnumPath<Category> category = createEnum("category", Category.class);

    public final CollectionPath<project.como.domain.comment.model.Comment, project.como.domain.comment.model.QComment> comment = this.<project.como.domain.comment.model.Comment, project.como.domain.comment.model.QComment>createCollection("comment", project.como.domain.comment.model.Comment.class, project.como.domain.comment.model.QComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> heartCount = createNumber("heartCount", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final NumberPath<Long> readCount = createNumber("readCount", Long.class);

    public final EnumPath<PostState> state = createEnum("state", PostState.class);

    public final ListPath<Tech, EnumPath<Tech>> techs = this.<Tech, EnumPath<Tech>>createList("techs", Tech.class, EnumPath.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public final project.como.domain.user.model.QUser user;

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new project.como.domain.user.model.QUser(forProperty("user")) : null;
    }

}

