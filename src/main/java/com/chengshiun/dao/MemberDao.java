package com.chengshiun.dao;

import com.chengshiun.dto.MemberRegisterRequest;
import com.chengshiun.model.Member;
import com.chengshiun.model.Role;

import java.util.List;

public interface MemberDao {

    // 基本 Member 操作
    Member getMemberById(Integer memberId);

    Member getMemberByEmail(String email);

    Integer getIdByUsername(String email);

    Integer createMember(MemberRegisterRequest memberRegisterRequest);

    List<Role> getRolesByMemberId(Integer memberId);

    Role getRoleByName(String roleName);

    void addRoleForMemberId(Integer memberId, Role role);

    void removeRoleForMemberId(Integer memberId, Role role);
}
