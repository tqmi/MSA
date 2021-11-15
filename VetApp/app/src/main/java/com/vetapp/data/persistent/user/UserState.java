package com.vetapp.data.persistent.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vetapp.data.models.user.User;

public class UserState {

    private static MutableLiveData<User> currentUser = new MutableLiveData<>(null);

    public static LiveData<User> getUserLive(){
        return currentUser;
    }

    public static User getCurrentUser(){
        return currentUser.getValue();
    }

    public static void setCurrentUser(User user){
        currentUser.setValue(user);
    }

    public static boolean isUserSignedIn() {
        return currentUser.getValue() != null;
    }
}
