package market.thunder.service;

import lombok.RequiredArgsConstructor;
import market.thunder.domain.Category;
import market.thunder.domain.Post;
import market.thunder.form.PostForm;
import market.thunder.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PostUpdate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    // 게시물 등록
    @Transactional
    public Long post(Post post){
        return postRepository.save(post).getId();
    }

    // 게시물 ID로 게시물 조회
    public Post findByPostId(Long postId){
        return postRepository.findById(postId).orElse(null);
    }

    // 페이지에 해당 하는 게시물 조회
    public List<Post> getPage(Category category, Pageable pageable) {

        List<Post> posts;

        if(category == null) {
            posts = postRepository.findAll(pageable).getContent();
        }
        else{
            posts = postRepository.findByCategory(category, pageable).getContent();
        }

        return posts;
    }

    // 게시물 수 카운트
    public int countPost(Category category) {
        if(category == null) {
            return (int)postRepository.count();
        }
        else{
            return postRepository.countByCategory(category).intValue();
        }

    }

    // 게시물 조회수 추가
    @Transactional
    public void countView(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        post.setView(post.getViews() + 1L);
    }

    // 게시물 업데이트
    @Transactional
    public void updatePost(Long postId, PostForm postForm){
        Post post = postRepository.findById(postId).orElse(null);
        post.updatePost(postForm);
    }

    // 게시물 삭제
    @Transactional
    public void delete(Long postId) {
        postRepository.delete(postRepository.findById(postId).orElse(null));
    }
}
