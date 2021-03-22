package project.securitydemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.securitydemo.Member.MemberAuthMap;
import project.securitydemo.Member.Member;
import project.securitydemo.Member.MemberService;


import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberService memberService;

    /** 인증 하는 부분 **/
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Member member = memberService.findByMemberId(memberId);
        return new User(member.getMemberId(), member.getMemberPassword(), getAuthorities(member));
    }

    /** 권한 받아오는 부분 **/
    private Collection<? extends GrantedAuthority> getAuthorities(Member member) {
        String[] userRoles =  convert(member.getMemberAuthMapList());
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
        return authorities;
    }

    /** 실제 권한 매핑 함수 */
    public String[] convert(List<MemberAuthMap> list)
    {
        String[] arrayOfString = new String[list.size()];
        int index = 0;
        for (MemberAuthMap memberAuthMap : list) {
            arrayOfString[index++] = memberAuthMap.getMemberAuth().getAuthority();
        }
        return arrayOfString;
    }

}