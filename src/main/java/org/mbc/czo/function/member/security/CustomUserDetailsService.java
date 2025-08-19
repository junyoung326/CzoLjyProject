package org.mbc.czo.function.member.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.czo.function.member.domain.Member;
import org.mbc.czo.function.member.repository.MemberJpaRepository;
import org.mbc.czo.function.member.security.dto.MemberSecurityDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // 패스워드를 암호화처리하도록 CustomSecurityConfig 구현

    private final MemberJpaRepository memberJpaRepository;
 /*   private PasswordEncoder passwordEncoder;


    public CustomUserDetailsService(){
       // CustomUserDetailsService() 호출되면 자동으로 패스워드를 암호화 처리 가능.
        this.passwordEncoder = new BCryptPasswordEncoder();
    }*/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 실제 인증 처리할 때 호출 되는 메서드
        // 프론트에서 id로 넘어오는 username 값을 처리한다.
        log.info("CustomUserDetailsService.loadUserByUsername메서드 호출 됨....");
        log.info("loadUserByUsername.로그온 사용자의 id :" + username);

        Optional<Member> result = memberJpaRepository.getWithRoles(username);
        // username이 들어가면 role까지 나옴

        if(result.isEmpty()){
            // 해당하는 정보가 db에 없으면
            throw new UsernameNotFoundException("username을 찾을 수 없습니다.");
        }

        Member member = result.get(); // 해당하는 member가 있으면 넣음



        MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                member.getMid(),
                member.getMpassword(),
                member.getMemail(),
                member.isMisActivate(),
                false,  //boolean social
                member.getMname(),
                member.getMphoneNumber(),
                member.getMaddress(),
                member.getMmileage(),
                member.getMroleSet().stream().map(memberRole ->
                                new SimpleGrantedAuthority("ROLE_"+memberRole.name()))
                        // ROLE_USER, ROLE_ADMIN
                        .collect(Collectors.toList() // ROLE_USER, ROLE_ADMIN
              )
        );
        log.info("CustomUserDetailsService.loadUserByUsername 메서드 실행.....");
        log.info("memberSecurityDTO :" + memberSecurityDTO);

        return memberSecurityDTO;
    }
}
