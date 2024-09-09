package com.chengshiun.rowmapper;

import com.chengshiun.model.Member;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MemberRowMapper implements RowMapper<Member> {

    @Override
    public Member mapRow(ResultSet resultSet, int i) throws SQLException {
        Member member = new Member();
        member.setMemberId(resultSet.getInt("member_id"));
        member.setEmail(resultSet.getString("email"));
        member.setPassword(resultSet.getString("password"));
        member.setName(resultSet.getString("name"));
        member.setAge(resultSet.getInt("age"));

        //處理 權限
        List<String> roleNames = new ArrayList<>();
        do {
            String roleName = resultSet.getString("role_name");
            if (roleName != null) {
                roleNames.add(roleName);
            }
        } while (resultSet.next());

        member.setRoleNames(roleNames);

        return member;
    }
}
