package market.thunder.form;

import lombok.Getter;
import lombok.Setter;
import market.thunder.domain.Category;
import market.thunder.domain.Member;
import market.thunder.domain.PostStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class PostForm {
    private String userId;

    private Category category;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "가격을 입력해주세요.")
    private Long price;

    private PostStatus postStatus;

    @NotEmpty(message = "사진은 1개 이상 3개 이하로 업로드해주세요.")
    @Size(min = 1, max=3, message = "사진은 1개 이상 3개 이하로 업로드해주세요.")
    String[] saveNames; // photoName
}
