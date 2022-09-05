package market.thunder.form;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostInfoForm {

    public PostInfoForm(){
        this.action = "/posts/write";
        this.actionMethod = "POST";
    }

    public PostInfoForm(Long postId){
        this.action = "/posts/" + postId + "/update";
        this.actionMethod = "PUT";
    }

    String action;
    String actionMethod;
}
