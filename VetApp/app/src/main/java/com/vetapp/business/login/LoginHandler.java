package com.vetapp.business.login;

import android.util.Patterns;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;
import com.vetapp.R;
import com.vetapp.data.login.LoginDataSource;
import com.vetapp.data.login.model.LoginResult;
import com.vetapp.ui.login.LoginFormState;

public class LoginHandler {

    private static LoginDataSource dataSource = new LoginDataSource();

    public static LiveData<FirebaseUser> getCurrentUser(){
        return dataSource.getCurrentUser();
    }

    // A placeholder username validation check
    private static boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private static boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public static LoginFormState loginDataChanged(String username, String password) {
        if (!LoginHandler.isUserNameValid(username)) {
            return new LoginFormState(R.string.invalid_username, null);
        } else if (!LoginHandler.isPasswordValid(password)) {
            return new LoginFormState(null, R.string.invalid_password);
        } else {
            return new LoginFormState(true);
        }
    }

    public static void login(String username, String password) {
        dataSource.login(username,password);
    }

    public static void logout(){
        dataSource.logout();
    }

    public static LiveData<LoginResult> getLoginResult() {
        return dataSource.getLoginResult();
    }
}
