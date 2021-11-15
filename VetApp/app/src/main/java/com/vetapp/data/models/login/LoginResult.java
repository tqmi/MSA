package com.vetapp.data.models.login;

public class LoginResult {

    private boolean success = false;
    private String errmsg;

    public LoginResult(boolean success, String errmsg) {
        this.success = success;
        this.errmsg = errmsg;
    }

    public LoginResult(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
