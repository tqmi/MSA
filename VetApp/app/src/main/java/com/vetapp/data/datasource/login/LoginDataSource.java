package com.vetapp.data.datasource.login;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vetapp.data.models.login.LoginResult;
import com.vetapp.data.models.user.User;
import com.vetapp.data.persistent.user.UserState;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private FirebaseAuth mAuth;
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<LoginResult>(null);

    public LoginDataSource(){
        mAuth = FirebaseAuth.getInstance();
        UserState.setCurrentUser(new User(mAuth.getCurrentUser()));
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
                            UserState.setCurrentUser(new User(user));
                            loginResult.setValue(new LoginResult(true));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInUserWithEmail:failure "+ task.getException().getMessage());
//                            Toast.makeText(null, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            UserState.setCurrentUser(null);
                            loginResult.setValue(new LoginResult(false,task.getException().getMessage()));
                        }
                    }
                });
    }
    public void logout(){
        if(!UserState.isUserSignedIn()){
            Log.w(TAG,"User not signed in");
            return;
        }
        mAuth.signOut();
        loginResult.setValue(null);
        UserState.setCurrentUser(null);
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }
}