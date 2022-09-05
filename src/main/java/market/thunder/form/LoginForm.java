package market.thunder.form;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class LoginForm {

    @NotEmpty
    private String userId;

    @NotEmpty
    private String password;
}
