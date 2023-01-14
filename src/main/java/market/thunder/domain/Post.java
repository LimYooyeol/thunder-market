package market.thunder.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import market.thunder.form.PostForm;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @Column(length = 100)
    private String title;

    @Column(length = 2000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Photo> photos = new ArrayList<>();

    private LocalDateTime postDate;
    private Long views; // 조회 수
    private Long price;
    private void setMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
    }

    public void setView(Long views){
        this.views =views;
    }

    public static Post createPost(Member member, Category category, String title, String content,
                                  Long price){
        Post post = new Post();

        post.setMember(member);
        post.category = category;
        post.title = title;
        post.content = content;
        post.price = price;

        post.status = PostStatus.SALE;
        post.views = 0L;
        post.postDate = LocalDateTime.now();

        return post;
    }

    public void updatePost(PostForm postForm) {
        this.category = postForm.getCategory();
        this.title = postForm.getTitle();
        this.content = postForm.getContent();

        this.price = postForm.getPrice();
        this.status = postForm.getPostStatus();

        this.postDate = LocalDateTime.now();
    }
}
