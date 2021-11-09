package com.vetapp.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.vetapp.business.login.LoginHandler;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();

    LoginViewModel() {

    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }


    public LiveData<FirebaseUser> getCurrentUser(){
        return LoginHandler.getCurrentUser();
    }

    public void login(String username, String password) {
        LoginHandler.login(username,password);
    }

    public void loginDataChanged(String username, String password) {
        loginFormState.setValue(LoginHandler.loginDataChanged(username,password));
    }


}