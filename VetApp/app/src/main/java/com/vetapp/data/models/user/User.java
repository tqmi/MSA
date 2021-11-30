package com.vetapp.data.models.user;

import com.google.firebase.auth.FirebaseUser;

public class User {

    private final FirebaseUser firebaseUser;

    public User(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    public boolean isSignedIn(){
        return firebaseUser != null;
    }

    public String getEmail(){
        if(firebaseUser != null)
            return firebaseUser.getEmail();
        return "";
    }

}
