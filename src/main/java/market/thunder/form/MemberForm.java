package market.thunder.form;

import lombok.Getter;
import lombok.Setter;
import market.thunder.repository.MemberRepository;
import market.thunder.service.MemberService;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "아이디를 입력해주세요.")
    private String userId;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotEmpty(message = "확인용 비밀번호를 입력해주세요.")
    private String checkPassword;

    public boolean isValid(){
        return password.equals(checkPassword);
    }
}
