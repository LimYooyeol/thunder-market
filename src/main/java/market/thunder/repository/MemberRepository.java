package market.thunder.repository;

import lombok.RequiredArgsConstructor;
import market.thunder.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    // 회원 저장
    public void save(Member member){
        em.persist(member);
    }

    // 회원 ID로 회원 조회
    public Member findByMemberId(Long memberId){
        return em.find(Member.class, memberId);
    }

    // 사용자 ID로 회원 조회
    public Member findByMemberUserId(String userId) {
        List<Member> users = em.createQuery("select m from Member m where m.userId = : userId", Member.class)
                .setParameter("userId", userId)
                .getResultList();

        if(users.isEmpty()){
            return null;
        }
        else{
            return users.get(0);
        }
    }
}
