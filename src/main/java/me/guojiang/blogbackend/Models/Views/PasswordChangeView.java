package me.guojiang.blogbackend.Models.Views;

public class PasswordChangeView {
    private String userName;
    private String oldPassword;
    private String newPassword;

    public static PasswordChangeView create() {
        return new PasswordChangeView();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
