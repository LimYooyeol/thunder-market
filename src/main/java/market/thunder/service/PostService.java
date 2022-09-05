package market.thunder.service;

import lombok.RequiredArgsConstructor;
import market.thunder.domain.Category;
import market.thunder.domain.Post;
import market.thunder.form.PostForm;
import market.thunder.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PostUpdate;
import java.awt.print.Pageable;
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
        return postRepository.save(post);
    }

    // 게시물 ID로 게시물 조회
    public Post findByPostId(Long postId){
        return postRepository.findByPostId(postId);
    }

    // 페이지에 해당 하는 게시물 조회
    public List<Post> getPage(Category category, int page) {

        List<Post> posts;

        if(category == null) {
            posts = postRepository.findAll();
        }
        else{
            posts = postRepository.findCategory(category);
        }

        int start = (page - 1) * 3;
        int end = start + 3;
        List<Post> pagePosts = new ArrayList<>();

        for(int i = start; i < end && i < posts.size(); i++){
            pagePosts.add(posts.get(i));
        }

        return pagePosts;
    }

    // 게시물 수 카운트
    public int countPost(Category category) {
        List<Post> posts;
        if(category == null) {
            posts = postRepository.findAll();
        }
        else{
            posts = postRepository.findCategory(category);
        }

        return posts.size();
    }

    // 게시물 조회수 추가
    @Transactional
    public void countView(Long postId) {
        Post post = postRepository.findByPostId(postId);
        post.setView(post.getViews() + 1L);
    }

    // 게시물 업데이트
    @Transactional
    public void updatePost(Long postId, PostForm postForm){
        Post post = postRepository.findByPostId(postId);
        post.updatePost(postForm);
    }

    // 게시물 삭제
    @Transactional
    public void delete(Long postId) {
        postRepository.delete(postId);
    }
}
