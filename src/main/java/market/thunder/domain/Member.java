package market.thunder.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "userId", unique = true)
    private String userId;
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    public static Member createMember(String userId, String password) {
        Member member = new Member();
        member.userId = userId;
        member.password = password;

        return member;
    }
}
