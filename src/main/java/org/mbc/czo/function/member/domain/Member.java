package org.mbc.czo.function.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.mbc.czo.function.member.constant.Role;
import org.mbc.czo.function.member.dto.MemberJoinDTO;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="member")
@Getter
@Setter
@Builder
@ToString

public class Member {

    @Id
    @NotNull
    @Column(name="member_id", length = 255)
    private String mid;

    @NotNull
    @Column(name="member_name", length = 10)
    private String mname;

    @NotNull
    @Column(name="member_phoneNumber", length = 11, unique = true)  // 유니크 처리
    private String mphoneNumber;

    @NotNull
    @Column(name="member_email", length = 30, unique = true)  // 유니크 처리
    private String memail;  // 회원 검색 처리용

    @NotNull
    @Column(name="member_password", length = 255) //암호화 시 글자 수 늘어남에 따른 length 증가
    private String mpassword;

    @NotNull
    @Column(name="member_address", length = 100)
    private String maddress;

    // 회원롤 관리(user,admin)
    @Column(name="member_roleSet")
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Role> mroleSet = new HashSet<Role>();

    @Column(name="member_mileage")
    private Long mmileage;

    @Column(name = "member_isActivate")
    private boolean misActivate;

    @Column(name = "member_isSocialActivate")
    private boolean misSocialActivate;

    public void addRole(Role role) {
        this.mroleSet.add(role);
    }

    @PrePersist //db 저장 전 실행(초기화)
    public void prePersist() {
        this.misActivate = true;
        this.misSocialActivate = false;
    }

    public static Member createMember(MemberJoinDTO memberJoinDTO, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setMid(memberJoinDTO.getMid());
        member.setMname(memberJoinDTO.getMname());
        member.setMphoneNumber(memberJoinDTO.getMphoneNumber());
        member.setMemail(memberJoinDTO.getMemail());
        member.setMpassword(passwordEncoder.encode(memberJoinDTO.getMpassword()));
        member.setMaddress(memberJoinDTO.getMaddress());
        member.addRole(Role.USER);
        return member;
    } // 회원 생성용 메서드 (dto와 암호화를 받아 Member 객체 리턴)

}
