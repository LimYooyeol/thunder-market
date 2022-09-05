package market.thunder.controller;

import lombok.RequiredArgsConstructor;
import market.thunder.domain.Comment;
import market.thunder.domain.Member;
import market.thunder.domain.Photo;
import market.thunder.domain.Post;
import market.thunder.form.CommentForm;
import market.thunder.form.PostForm;
import market.thunder.form.PostInfoForm;
import market.thunder.service.CommentService;
import market.thunder.service.MemberService;
import market.thunder.service.PhotoService;
import market.thunder.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PostUpdate;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberService memberService;
    private final CommentService commentService;

    private final PhotoService photoService;

    /*
        게시물 작성 요청
   */
    @GetMapping("/posts/write")
    public String postForm(Model model, HttpSession session){
        String userId = (String)session.getAttribute("userId");
        if(userId == null){
            return "redirect:/login";
        }


        PostForm postForm = new PostForm();

        PostInfoForm postInfoForm = new PostInfoForm();
        postInfoForm.setAction("/posts/write");
        postInfoForm.setActionMethod("POST");

        model.addAttribute("userId", userId);
        model.addAttribute("postForm", postForm);
        model.addAttribute("postInfoForm", postInfoForm);

        return "posts/postForm";
    }

    /*
    *   게시물 등록 처리
    * */
    @PostMapping("/posts/write")
    public String post(@ModelAttribute @Valid PostForm postForm, BindingResult result, Model model, HttpSession session){
        if(result.hasErrors()){
            model.addAttribute("userId", session.getAttribute("userId"));
            model.addAttribute(new PostInfoForm());
            return "posts/postForm";
        }

        Post post = Post.createPost(memberService.findByUserId((String) session.getAttribute("userId"))
                , postForm.getCategory(), postForm.getTitle(),
                postForm.getContent(), postForm.getPrice());

        postService.post(post);

        photoService.matchPost(postForm.getSaveNames(), post);

        return "redirect:/?category=" + post.getCategory().toString();
    }

    /*
    *   게시물 보기
    * */
    @GetMapping("/posts/{postId}")
    public String getPost(@PathVariable("postId") Long postId, Model model, HttpSession session){
        Post post = postService.findByPostId(postId);
        if(post == null){
            return "redirect:/";
        }

        postService.countView(postId);

        List<Comment> comments = post.getComments();

        comments.sort(new Comparator<Comment>(){
            @Override
            public int compare(Comment o1, Comment o2) {
                if(o1.getGroupId() != o2.getGroupId()){
                    return o1.getGroupId().compareTo(o2.getGroupId());
                }
                else{
                    return o1.getId().compareTo(o2.getId());
                }
            }
        });

        model.addAttribute("post", post);
        model.addAttribute("commentForm", new CommentForm());

        if(session.getAttribute("userId") != null){
            model.addAttribute("userId", session.getAttribute("userId"));
        }

        return "posts/postView";
    }


    /*
    *   게시물 수정 페이지 연결
    * */
    @GetMapping("posts/{postId}/update")
    public String updateRequest(@PathVariable("postId") Long postId, Model model){
        Post post = postService.findByPostId(postId);

        PostForm postForm = new PostForm();

        // 기존 게시물 정보
        postForm.setUserId(post.getMember().getUserId());
        postForm.setCategory(post.getCategory());
        postForm.setTitle(post.getTitle());
        postForm.setContent(post.getContent());

        postForm.setPrice(post.getPrice());
        postForm.setPostStatus(post.getStatus());

        String[] saveNames = new String[post.getPhotos().size()];
        for(int i = 0 ; i < saveNames.length; i++){
            saveNames[i] = post.getPhotos().get(i).getSaveName();
        }
        postForm.setSaveNames(saveNames);


        model.addAttribute("postForm", postForm);
        model.addAttribute("postId", postId);
        model.addAttribute("postInfoForm", new PostInfoForm(postId));

        return "posts/postForm";

    }

    /*
    *   게시물 수정
    * */
    @PutMapping("posts/{postId}/update")
    public String updatePost(@PathVariable("postId") Long postId, @Valid PostForm postForm, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute("postId", postId);
            model.addAttribute("postInfoForm", new PostInfoForm(postId));

            return "posts/postForm";
        }

        postService.updatePost(postId, postForm);
        Post post = postService.findByPostId(postId);

        String[] saveNames = postForm.getSaveNames();

        // 저장되어 있던 파일 이름 중, 수정되면서 없어진 것은 연결관계를 끊기
        String[] originNames = new String[post.getPhotos().size()];
        for(int i = 0 ; i < originNames.length; i++){
            originNames[i] = post.getPhotos().get(i).getSaveName();
        }
        for(String originName : originNames){
            boolean keep = false;
            for(String saveName : saveNames){
                if(originName.equals(saveName)){
                    keep = true;
                }
            }
            if(!keep){
                photoService.unMatch(originName);
            }
        }

        photoService.matchPost(saveNames, post);
        return "redirect:/posts/" + postId;
    }

    /*
    *  게시물 삭제
    * */
    @DeleteMapping("posts/{postId}/delete")
    public String deletePost(@PathVariable("postId") Long postId){
        postService.delete(postId);

        return "redirect:/";
    }


}
