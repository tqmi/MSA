package com.vetapp.data.login;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private FirebaseAuth mAuth;
    private MutableLiveData<FirebaseUser> currentUser = new MutableLiveData<>(null);

    public LoginDataSource(){
        mAuth = FirebaseAuth.getInstance();
    }

    public LiveData<FirebaseUser> getCurrentUser(){
        return currentUser;
    }

    public void login(String username, String password) {
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            currentUser.setValue(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInUserWithEmail:failure", task.getException());
//                            Toast.makeText(null, "Authentication failed.", Toast.LENGTH_SHORT).show();

                            currentUser.setValue(null);
                        }
                    }
                });
    }
    public void logout(){
        if(currentUser.getValue() == null){
            Log.w(TAG,"User not signed in");
            return;
        }
        mAuth.signOut();
        currentUser.setValue(null);
    }
}