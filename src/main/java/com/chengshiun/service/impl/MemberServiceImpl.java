package com.chengshiun.service.impl;

import com.chengshiun.dao.MemberDao;
import com.chengshiun.dto.MemberRegisterRequest;
import com.chengshiun.model.Member;
import com.chengshiun.model.Role;
import com.chengshiun.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Member getMemberById(Integer memberId) {
        //
        return memberDao.getMemberById(memberId);
    }

    @Override
    public Member getMemberByEmail(String email) {
        return memberDao.getMemberByEmail(email);
    }

    @Override
    public Integer getIdByUsername(String email) {
        return memberDao.getIdByUsername(email);
    }

    @Override
    public Integer createMember(MemberRegisterRequest memberRegisterRequest) {
        //檢查 email 是否已被註冊過
        Member member = memberDao.getMemberByEmail(memberRegisterRequest.getEmail());

        if (member != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //密碼加密
        String hashedPassword = passwordEncoder.encode(memberRegisterRequest.getPassword());
        memberRegisterRequest.setPassword(hashedPassword);

        Integer memberId = memberDao.createMember(memberRegisterRequest);

        return memberId;
    }

    @Override
    public List<Role> getRolesByMemberId(Integer memberId) {
        return memberDao.getRolesByMemberId(memberId);
    }

    @Override
    public Role getRoleByName(String roleName) {
        return memberDao.getRoleByName(roleName);
    }

    @Override
    public void addRoleForMemberId(Integer memberId, Role role) {
        memberDao.addRoleForMemberId(memberId, role);
    }

    @Override
    public void removeRoleForMemberId(Integer memberId, Role role) {
        memberDao.removeRoleForMemberId(memberId, role);
    }
}
