package com.vetapp.data.persistent.user;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vetapp.data.datasource.user.UserDataSource;
import com.vetapp.data.models.login.LoginResult;
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

    static {
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!= null){
                    UserDataSource.loadUser(firebaseAuth.getCurrentUser(), new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            UserData data = task.getResult().toObject(UserData.class);

                            UserState.setLoggedInUser(firebaseAuth.getCurrentUser(), data);
                        }
                    });
                }
                else{
                    UserState.setLoggedInUser(null,null);
                }
            }
        });
    }
}
