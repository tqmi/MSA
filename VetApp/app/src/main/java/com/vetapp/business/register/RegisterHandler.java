package com.vetapp.business.register;

import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vetapp.R;
import com.vetapp.business.login.LoginHandler;
import com.vetapp.data.datasource.register.RegisterDataSource;
import com.vetapp.data.datasource.user.UserDataSource;
import com.vetapp.data.models.register.RegisterData;
import com.vetapp.data.models.register.RegisterResult;
import com.vetapp.data.models.user.User;
import com.vetapp.data.models.user.UserData;
import com.vetapp.ui.login.LoginFormState;
import com.vetapp.ui.register.RegisterFormState;

public class RegisterHandler {

    public static boolean isEmailValid(String email){
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return false;
        }
    }

    private static boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public static void register(RegisterData data, MutableLiveData<RegisterResult> result){
        RegisterDataSource ds = new RegisterDataSource();
        if(isEmailValid(data.getEmail()) && isPasswordValid(data.getPassword()))
            ds.register(data, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d( null,"createUserWithEmail:success");

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        UserDataSource.setUser(new User(user, getUserData(data)), new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                FirebaseAuth.getInstance().signOut();
                                result.setValue(new RegisterResult(true));
                            }
                        });
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(null, "createUserWithEmail:failure", task.getException());
                        result.setValue(new RegisterResult(false,task.getException().getMessage()));
                    }
                }
            });
    }


    public static RegisterFormState registerDataChanged(RegisterData data) {
        if (!isEmailValid(data.getEmail())) {
            return new RegisterFormState(R.string.invalid_username, null);
        } else if (!isPasswordValid(data.getPassword())) {
            return new RegisterFormState(null, R.string.invalid_password);
        } else {
            return new RegisterFormState(true);
        }
    }


    private static UserData getUserData(RegisterData data){
        return  new UserData(data.getEmail(), data.getFirstName(), data.getLastName(), data.getType(), data.getPhone());
    }
}
