package com.github.library_app_boot.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int memberId;

    @Column(name = "username")
    @NotEmpty(message = "Username should not be empty!")
    private String username;

    @Column(name = "password")
    @NotEmpty(message = "Password should not be empty!")
    private String password;

    public Member() {}

    public Member(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
