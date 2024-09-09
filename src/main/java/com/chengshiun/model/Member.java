package com.chengshiun.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Member {

    private Integer memberId;
    private String email;

    @JsonIgnore
    private String password;

    private String name;
    private Integer age;

    private List<String> roleNames;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List<String> roleName) {
        this.roleNames = roleName;
    }
}
