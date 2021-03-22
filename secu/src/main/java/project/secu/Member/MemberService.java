package project.demo.domain.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService{

    @Autowired
    MemberRepository memberRepository;

    public Member findByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId);
    }

}