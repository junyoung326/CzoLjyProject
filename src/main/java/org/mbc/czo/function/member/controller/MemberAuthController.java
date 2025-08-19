package org.mbc.czo.function.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.czo.function.member.dto.MemberJoinDTO;
import org.mbc.czo.function.member.dto.MemberResetPWDTO;
import org.mbc.czo.function.member.service.MemberAuthServiceImpl;
import org.mbc.czo.function.member.service.MemberAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@Controller
@RequiredArgsConstructor // final을 붙인 필드로 생성자 만듬.

//member의 인증(Authentication) 로직 담당
public class MemberAuthController {

    private final MemberAuthService memberAuthService;

    @GetMapping("/member/login")
    public void get_MemberLogin(String error, String logout) {

        // http://localhost/member/login?error=???
        // http://localhost/member/login?logout=???
        log.info("MemberAuthController.loginGet메서드 실행....");
        log.info("logout: " + logout); //데이터베이스에서 활용
        log.info("error: " + error); //데이터베이스에서 활용

        if(logout != null){
            log.info("logout 처리됨!!! : " + logout);
        }
    }

    @GetMapping("/member/join")
    public String get_MemberJoin() {
        return "member/join";
    }

    @PostMapping("/member/join")
    public String post_MemberJoin(MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes){
        // html에서 넘어오는 데이터 처리용

        log.info("MemberAuthController.post_MemberJoin..... ");
        //log.info(memberDTO);

        try{
            memberAuthService.join(memberJoinDTO); // 회원가입 처리됨!!

            //id 중복되면 MidExistException예외처리 발생
        }catch(MemberAuthServiceImpl.M_AuthException e){

            //redirectAttributes.addFlashAttribute("error","mid");
            // 회원가입시 id 중복되는 예외처리
            return "redirect:/member/join"; // 회원가입 페이지로 다시 감.

        }
        //redirectAttributes.addFlashAttribute("result","success");
        return "redirect:/member/login"; //회원가입 성공시 로그인 페이지로 이동
    }

    @GetMapping("/member/resetPW")
    public String get_MemberResetPW() {
        return "member/resetPW";
    }
    //임시로 modify기능 여기다가 구현
    @PostMapping("/member/resetPW")
    public String post_MemberResetPW(
            MemberResetPWDTO memberResetPWDTO,
            @RequestParam("changePWAccountID") String changePWAccountID,
            RedirectAttributes redirectAttributes){

        log.info("MemberAuthController.post_MemberResetPW..... ");
        log.info(memberResetPWDTO);

        try{
            memberAuthService.resetPW(memberResetPWDTO, changePWAccountID);

        }catch(MemberAuthServiceImpl.M_AuthException e){

            // 실패 메시지 전달
            redirectAttributes.addFlashAttribute("changePWAccountID", changePWAccountID);
            redirectAttributes.addFlashAttribute("message", "새로운 비밀번호와 비밀번호 확인이 다릅니다");
            redirectAttributes.addFlashAttribute("status", "error");
            return "redirect:/member/resetPW"; //실패

        }

        return "redirect:/member/resetPW"; //성공
    }
}
