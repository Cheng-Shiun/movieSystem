package com.chengshiun.controller;

import com.chengshiun.dto.MemberRegisterRequest;
import com.chengshiun.model.Member;
import com.chengshiun.model.Role;
import com.chengshiun.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<Member> register(@RequestBody @Valid MemberRegisterRequest memberRegisterRequest) {

        Integer memberId = memberService.createMember(memberRegisterRequest);

        //預設給註冊成功的 member 添加 normal_member 的 role
        Role normalMemberRole = memberService.getRoleByName("ROLE_NORMAL_MEMBER");
        memberService.addRoleForMemberId(memberId, normalMemberRole);

        Member member = memberService.getMemberById(memberId);

        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    // /login api 預設是由 Spring Security 管理，除非去修改預設
    @PostMapping("/userLogin")
    public String login(Authentication authentication, HttpSession session) {
        //帳號密碼驗證由 Spring Security 處理，能執行到這裡表示已經登入認證成功

        //取得使用者的帳號 (這邊的name邏輯是 email 值)
        String username = authentication.getName();

        //將登入後的使用者 id 存在 session
        Integer memberId = memberService.getIdByUsername(username);
        session.setAttribute("memberId", memberId);

        //取得使用者的權限 (這邊因沒使用 role table 所以為空)
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        return "登入成功！帳號 " + username + " 權限為: " + authorities;
    }
}
