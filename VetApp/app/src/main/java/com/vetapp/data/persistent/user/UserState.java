package com.vetapp.data.persistent.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.vetapp.data.models.user.User;
import com.vetapp.data.models.user.UserData;

public class UserState {

    private static MutableLiveData<User> currentUser = new MutableLiveData<>(new User(null,null));

    public static LiveData<User> getUserLive(){
        return currentUser;
    }

    public static User getCurrentUser(){
        return currentUser.getValue();
    }

    public static boolean isUserSignedIn() {
        return currentUser.getValue().isSignedIn();
    }

    public static void setLoggedInUser(FirebaseUser user,UserData data){
        currentUser.setValue(new User(user,data));
    }

    public static String getUID(){
        if(currentUser.getValue() != null)
            return currentUser.getValue().getUID();
        return null;
    }
}
