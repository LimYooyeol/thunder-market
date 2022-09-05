package market.thunder.domain;

import lombok.Getter;
import market.thunder.form.CommentForm;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
public class Comment {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Long groupId;

    @Column(length = 500)
    private String content;

    private LocalDateTime commentDate;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;

    public void setPost(Post post){
        this.post = post;
        post.getComments().add(this);
    }

    public static Comment createComment(Post post, Member member, Long groupId, String content){
        Comment comment = new Comment();
        comment.setPost(post);
        comment.member = member;
        comment.groupId = groupId;
        comment.content = content;
        comment.commentDate = LocalDateTime.now();
        comment.status = CommentStatus.NORMAL;

        return comment;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", post=" + post.getId() +
                ", member=" + member.getId() +
                ", groupId=" + groupId +
                ", content='" + content + '\'' +
                ", commentDate=" + commentDate +
                '}';
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateStatus(CommentStatus deleted) {
        this.status = deleted;
    }
}
