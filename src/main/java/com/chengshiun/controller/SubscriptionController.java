package com.chengshiun.controller;

import com.chengshiun.model.Role;
import com.chengshiun.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SubscriptionController {

    @Autowired
    private MemberService memberService;

    //訂閱 -> 賦予 vip_member role
    @PostMapping("/subscribe")
    public String subscribe(HttpSession session) {
        Integer memberId = (Integer) session.getAttribute("memberId");


        List<Role> roleList = memberService.getRolesByMemberId(memberId);

        //檢查 member 訂閱狀態 (是否有 vip_member role)
        boolean isSubscribed = checkSubscribeStatus(roleList);

        if (isSubscribed) {
            return "已經訂閱過，無法重複訂閱";
        } else {
            //新增 vip_member role
            Role vipRole = memberService.getRoleByName("ROLE_VIP_MEMBER");
            memberService.addRoleForMemberId(memberId, vipRole);

            // 更新 SecurityContext 中的權限
            updateSessionRoles(session, memberId);
        }
        return "訂閱成功！";
    }

    //取消訂閱 -> 移除 vip_member role
    @PostMapping("/unsubscribe")
    public String unsubscribe(HttpSession session) {
        Integer memberId = (Integer) session.getAttribute("memberId");

        List<Role> roleList = memberService.getRolesByMemberId(memberId);

        //檢查 member 訂閱狀態 (是否有 vip_member role)
        boolean isSubscribed = checkSubscribeStatus(roleList);

        if (isSubscribed) {
            //移除 vip_member role
            Role vipRole = memberService.getRoleByName("ROLE_VIP_MEMBER");
            memberService.removeRoleForMemberId(memberId, vipRole);

            //更新 SecurityContext 中的權限
            updateSessionRoles(session, memberId);

            return "已取消訂閱！";
        } else {
            return "尚未訂閱，無法執行取消訂閱操作";
        }

    }

    //自定義 checkSubscribeStatus()
    private boolean checkSubscribeStatus(List<Role> roleList) {
        boolean isSubscribed = false;

        for (Role role : roleList) {
            if (role.getRoleName().equals("ROLE_VIP_MEMBER")) {
                isSubscribed = true;
            }
        }
        return isSubscribed;
    }

    //自定義更新 Session 中的角色信息
    private void updateSessionRoles(HttpSession session, Integer memberId) {
        //取得更新後的 updateRoles
        List<Role> updatedRoles = memberService.getRolesByMemberId(memberId);

        //取得目前登入的 Authentication
        //SecurityContextHolder Class 為 Spring Security 儲存使用者的權限
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        //創建新的 Authentication ，並帶有更新後的角色
        List<GrantedAuthority> updatedAuthorities = new ArrayList<>();
        for (Role role : updatedRoles) {
            updatedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }

        //創建新的 Authentication
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                auth.getPrincipal(),
                auth.getCredentials(),
                updatedAuthorities
        );

        //設置更新後的 Authentication 到 SecurityContext 中
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
