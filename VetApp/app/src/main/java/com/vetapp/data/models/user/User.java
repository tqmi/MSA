package com.vetapp.data.models.user;

import com.google.firebase.auth.FirebaseUser;

public class User {

    private FirebaseUser firebaseUser;

    public User(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }
}
