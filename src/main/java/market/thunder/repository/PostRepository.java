package market.thunder.repository;

import market.thunder.domain.Category;
import market.thunder.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    public Page<Post> findAll(Pageable pageable);

    public Page<Post> findByCategory(Category category, Pageable pageable);

    public Long countByCategory(Category category);
}
