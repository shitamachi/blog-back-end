package me.guojiang.blogbackend.Models.Views;

import me.guojiang.blogbackend.Models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountInfoView {
    private String userName;
    private String nickName;
    private String email;
    private String avatar;
    private LocalDateTime createTime;
    private List<String> userGroups;

    public AccountInfoView() {
    }

    private AccountInfoView(String userName, String nickName, String email, String avatar, LocalDateTime createTime, List<String> userGroups) {
        this.userName = userName;
        this.nickName = nickName;
        this.email = email;
        this.avatar = avatar;
        this.createTime = createTime;
        this.userGroups = userGroups;
    }

    public static AccountInfoView createFromUserEntity(User user) {
        var roleList = new ArrayList<String>();
        user.getAuthorities().forEach(auth ->
                roleList.add(auth.getAuthority().toLowerCase().replace("role_", "")));
        return new AccountInfoView(user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getAvatar(),
                user.getCreateTime(),
                roleList);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public List<String> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<String> userGroups) {
        this.userGroups = userGroups;
    }
}
