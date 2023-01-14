package market.thunder.repository;

import market.thunder.domain.Category;
import market.thunder.domain.Member;
import market.thunder.domain.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostRepositoryTest {
    @Autowired PostRepository postRepository;
    @Autowired MemberRepository memberRepository;


    @Test
    public void 저장_테스트(){
        //given
        Member member = memberRepository.findByMemberId(1L);
        Post post = Post.createPost(member, Category.CLOTHES,
                "테스트1",
                "내용1",
                20000L);

        //when
        Post savePost = postRepository.save(post);
        Post findPost = postRepository.findById(savePost.getId()).orElse(null);

        //then
        assertEquals(findPost, post);
    }

    @Test
    public void 삭제_테스트(){
        //given
        Member member = memberRepository.findByMemberId(1L);
        Post post = Post.createPost(member, Category.CLOTHES,
                "테스트1",
                "내용1",
                20000L);


        //when
        Post savePost = postRepository.save(post);
        postRepository.delete(savePost);

        //then
        assertNull(postRepository.findById(savePost.getId()).orElse(null));
    }

    @Test
    public void 페이징_테스트_전체(){
        //given

        Sort.Order order1 = new Sort.Order(Sort.Direction.DESC, "status");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "postDate");
        Sort sort = Sort.by(order1, order2);
        Pageable pageable = PageRequest.of(1, 3,  sort);

        //when
        List<Post> postList = postRepository.findAll(pageable).getContent();

        //then
        assertEquals(3, postList.size());
        System.out.println(postList);
    }

    @Test
    public void 페이징_테스트_카테고리(){
        //given

        Sort.Order order1 = new Sort.Order(Sort.Direction.DESC, "status");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "postDate");
        Sort sort = Sort.by(order1, order2);
        Pageable pageable = PageRequest.of(1 - 1, 3,  sort);

        //when
        List<Post> postList = postRepository.findByCategory(Category.CLOTHES, pageable).getContent();

        //then
        assertEquals(3, postList.size());
        System.out.println(postList);
    }

    @Test
    public void 카운트_테스트(){
        //given
        Category category = Category.CLOTHES;

        //when
        Long count = postRepository.countByCategory(category);

        //then
        System.out.println(count);

    }
}