package com.vetapp.data.models.register;

public class RegisterResult {
    private boolean success = false;
    private String errmsg;

    public RegisterResult(boolean success, String errmsg) {
        this.success = success;
        this.errmsg = errmsg;
    }

    public RegisterResult(boolean success) {
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
