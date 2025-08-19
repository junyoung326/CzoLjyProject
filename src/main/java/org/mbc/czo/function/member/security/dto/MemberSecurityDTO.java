package org.mbc.czo.function.member.security.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.mbc.czo.function.member.constant.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
@Setter
@ToString

public class MemberSecurityDTO extends User implements OAuth2User {

    private String mid;

    private String mname;

    private String mphoneNumber;

    private String memail;  // 회원 검색 처리용

    private String mpassword;

    private String maddress;

    private Set<Role> mroleSet = new HashSet<Role>();

    private Long mmileage;

    private boolean misActivate;

    private boolean misSocialActivate;

    private Map<String, Object> mSocialprops;

    // 생성자
    public MemberSecurityDTO(String username, String password, String email,
                             boolean del, boolean social,
                             String mname,  String mphoneNumber, String maddress, Long mmileage,
                             Collection<? extends GrantedAuthority> authorities) {

        super(username, password, authorities);  // User 객체

        this.mid = username;
        this.mpassword = password;
        this.memail = email;
        this.misActivate = del;
        this.misSocialActivate = social;
        this.mname = mname;
        this.mphoneNumber = mphoneNumber;
        this.maddress = maddress;
        this.mmileage = mmileage;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.getMSocialprops();
    }

    @Override
    public String getName() {
        return this.mid;
    }
}
