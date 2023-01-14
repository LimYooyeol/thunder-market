package market.thunder.controller;

import lombok.RequiredArgsConstructor;
import market.thunder.domain.Category;
import market.thunder.domain.Post;
import market.thunder.form.PagingInfo;
import market.thunder.service.PostService;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;


    // 홈페이지
    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(required = false) Category category,
                       Model model, HttpSession session){
        if(session.getAttribute("userId") != null){
            model.addAttribute("userId", session.getAttribute("userId"));
        }

        int totalPages = ((postService.countPost(category) - 1) / 3) + 1;

        if(page <= 0){
            page = 1;
        }
        else if(page > totalPages){
            page = totalPages;
        }

        Sort.Order order1 = new Sort.Order(Sort.Direction.DESC, "status");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "postDate");
        Sort sort = Sort.by(order1, order2);

        Pageable pageable = PageRequest.of(page - 1, 3, sort);

        List<Post> posts = postService.getPage(category, pageable);

        model.addAttribute("category", category);
        model.addAttribute("posts", posts);
        model.addAttribute("pagingInfo",
                new PagingInfo(page, totalPages));

        return "home";
    }
}
