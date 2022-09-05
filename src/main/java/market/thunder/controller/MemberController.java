package market.thunder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import market.thunder.domain.Member;
import market.thunder.form.LoginForm;
import market.thunder.form.MemberForm;
import market.thunder.service.MemberService;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 로그인 페이지 요청
    @GetMapping("/login")
    public String loginForm(Model model, LoginForm loginForm){
        model.addAttribute("loginForm", loginForm);
        return "login";
    }

    // 로그인 정보 일치 검사
    @PostMapping("/login-check")
    @ResponseBody
    public String checkLogin(LoginForm loginForm){
        JSONObject response = new JSONObject();

        if(memberService.login(loginForm.getUserId(), loginForm.getPassword())){
            response.put("check", true);
        }
        else{
            response.put("check", false);
        }

        // 일치 : true, 실패 : false
        return response.toString();
    }

    // 로그인 시도
    @PostMapping("/login")
    public String login(@Valid LoginForm loginForm, BindingResult result, HttpSession httpSession) {

        // 항목이 비어있거나, ID 비밀번호가 일치하지 않는 경우
        if(result.hasErrors() || !memberService.login(loginForm.getUserId(), loginForm.getPassword())){
            return "/login";
        }

        // 로그인 성공
        httpSession.setAttribute("userId", loginForm.getUserId());
        return "redirect:/";
    }

    // 회원가입 페이지 요청
    @GetMapping("/members/signup")
    public String signupForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "members/signup";
    }

    // 회원가입 시도
    @PostMapping("members/signup")
    public String signup(@Valid MemberForm form, BindingResult result, Model model){

        // 내용이 비어있거나, 확인용 비밀번호가 일치하지 않는 경우
        if(result.hasErrors() || !form.isValid()){
            return "members/signup";
        }

        Member member = Member.createMember(form.getUserId(), form.getPassword());

        try{
            memberService.join(member);
        }
        catch (Exception e) {
            // ID가 중복되는 경우
            model.addAttribute("duplicationMessage", "ID 중복");
            return "members/signup";
        }

        // 회원가입 성공
        return "redirect:/";
    }

    // 로그아웃 요청
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

}
