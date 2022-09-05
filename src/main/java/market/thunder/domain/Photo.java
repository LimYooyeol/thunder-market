package market.thunder.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Photo {
    @Id
    @Column(name = "photo_id")
    private String saveName; // UUID

    private String originalName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public void setPost(Post post){
        if(this.post != null){ // 기존에 post가 존재하던 경우
             this.post.getPhotos().remove(this);
        }

        this.post = post;
        if(post != null) {
            post.getPhotos().add(this);
        }
    }
}
