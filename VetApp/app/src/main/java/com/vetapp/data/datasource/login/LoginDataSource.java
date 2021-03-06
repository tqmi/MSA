package com.vetapp.data.datasource.login;

import static com.vetapp.business.util.LogHelper.getTag;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vetapp.data.datasource.user.UserDataSource;
import com.vetapp.data.models.login.LoginResult;
import com.vetapp.data.models.user.UserData;
import com.vetapp.data.persistent.user.UserState;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private FirebaseAuth mAuth;
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<LoginResult>(null);

    public LoginDataSource(){
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            mAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        UserDataSource.loadUser(mAuth.getCurrentUser(), new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                UserData data = task.getResult().toObject(UserData.class);
                                UserState.setLoggedInUser(mAuth.getCurrentUser(),data);
                                loginResult.setValue(new LoginResult(true));
                            }
                        });
                    }
                }
            });

        }
    }

    public void login(String username, String password) {
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(getTag(), "signInUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserDataSource.loadUser(user, new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    UserData data = task.getResult().toObject(UserData.class);

                                    UserState.setLoggedInUser(user,data);
                                    loginResult.setValue(new LoginResult(true));
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(getTag(), "signInUserWithEmail:failure " + task.getException().getMessage());
//                            Toast.makeText(null, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            UserState.setLoggedInUser(null,null);
                            loginResult.setValue(new LoginResult(false,task.getException().getMessage()));
                        }
                    }
                });
    }
    public void logout(){
        if(!UserState.isUserSignedIn()){
            Log.w(getTag(), "User not signed in");
            return;
        }
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Log.d(getTag(), "here");
                    loginResult.setValue(null);
                    UserState.setLoggedInUser(null,null);
                }
            }
        });

        mAuth.signOut();
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }
}