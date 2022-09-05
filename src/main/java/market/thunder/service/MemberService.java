package market.thunder.service;

import lombok.RequiredArgsConstructor;
import market.thunder.domain.Member;
import market.thunder.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 추가
    @Transactional
    public void join(Member member) {
        memberRepository.save(member);
    }

    // 사용자 ID로 회원 조회
    public Member findByUserId(String userId){
        return memberRepository.findByMemberUserId(userId);
    }

    // 사용자 ID 중복 검사
    public Boolean checkUserIdDuplication(String userId){
        return (memberRepository.findByMemberUserId(userId) != null);
    }

    // 로그인 정보 일치 여부 검사
    public boolean login(String userId, String password) {
        Member findMember = memberRepository.findByMemberUserId(userId);

        if(findMember == null) {
            return false;
        }
        else{
            return findMember.getPassword().equals(password);
        }
    }
}
