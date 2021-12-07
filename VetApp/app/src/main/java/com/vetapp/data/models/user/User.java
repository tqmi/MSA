package com.vetapp.data.models.user;

import com.google.firebase.auth.FirebaseUser;

public class User {

    private final FirebaseUser firebaseUser;
    private final UserData data;

    public User(FirebaseUser firebaseUser,UserData data) {
        this.firebaseUser = firebaseUser;
        this.data = data;
    }

    public boolean isSignedIn(){
        return firebaseUser != null;
    }

    public String getEmail(){
        if(firebaseUser != null)
            return firebaseUser.getEmail();
        return "";
    }

    public String getUID(){
        if(firebaseUser != null){
            return firebaseUser.getUid();
        }
        return null;
    }

    public UserData getData() {
        return data;
    }

}
