package market.thunder.repository;

import lombok.RequiredArgsConstructor;
import market.thunder.domain.Category;
import market.thunder.domain.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {
    private final EntityManager em;

    // 게시물 저장
    public Long save(Post post){
        em.persist(post);
        return post.getId();
    }

    // 전체 게시물 조회
    public List<Post> findAll() {
        return em.createQuery("select p from Post p ORDER BY p.status DESC, p.postDate DESC", Post.class).getResultList();
    }

    // 게시물 ID로 게시물 조회
    public Post findByPostId(Long postId){
        return em.find(Post.class, postId);
    }

    // 게시물 삭제
    public void delete(Long postId) {
        em.remove(findByPostId(postId));
    }

    // 카테고리에 해당하는 게시물 조회
    public List<Post> findCategory(Category category) {
        return em.createQuery("select p from Post p where p.category =: category ORDER BY p.status DESC, p.postDate DESC")
                .setParameter("category", category)
                .getResultList();
    }
}
