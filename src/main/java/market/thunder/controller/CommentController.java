package market.thunder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import market.thunder.domain.Category;
import market.thunder.domain.Comment;
import market.thunder.domain.Member;
import market.thunder.domain.Post;
import market.thunder.form.CommentForm;
import market.thunder.service.CommentService;
import market.thunder.service.MemberService;
import market.thunder.service.PostService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final MemberService memberService;


    // 게시물 번호로 게시물에 달린 댓글 목록 불러오기
    @ResponseBody
    @GetMapping("posts/{postId}/comments")
    public String getCommentsByPostId(@PathVariable("postId")Long postId){
        List<Comment> comments = commentService.findCommentsByPostId(postId);

        JSONArray jsonArray = new JSONArray();

        for(Comment comment : comments){
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("commentId", comment.getId().toString());
            jsonObject.put("userId", comment.getMember().getUserId());
            jsonObject.put("content", comment.getContent());
            jsonObject.put("groupId", comment.getGroupId().toString());
            jsonObject.put("status", comment.getStatus().toString());
            jsonObject.put("commentDate", comment.getCommentDate().toString());

            jsonArray.add(jsonObject);
        }
        // 댓글 내용 JSON 형태로 반환
        return jsonArray.toJSONString();
    }


    // 댓글 업로드(작성)
    @ResponseBody
    @PostMapping("/comments/upload")
    public void comment(CommentForm commentForm){
        Post post = postService.findByPostId(commentForm.getPostId());
        Member member = memberService.findByUserId(commentForm.getUserId());

        Comment comment = Comment.createComment(post, member,
                 commentForm.getGroupId(), commentForm.getContent());

        commentService.add(comment);
    }

    // 댓글 수정
    @ResponseBody
    @PutMapping("/comments/{commentId}")
    public String updateComment(@PathVariable("commentId") Long commentId, HttpServletRequest httpServletRequest) throws IOException {
        ServletInputStream inputStream = httpServletRequest.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        Map<String, Object> json = new ObjectMapper().readValue(messageBody, HashMap.class);
        String newContent = (String)json.get("newContent");


        commentService.updateComment(commentId, newContent);

        return "ok";
    }

    // 댓글 삭제
    @ResponseBody
    @DeleteMapping("/comments/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId){
        commentService.delete(commentId);

        return "ok";
    }

}
