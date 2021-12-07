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
import com.vetapp.data.datasource.register.RegisterDataSource;
import com.vetapp.data.models.register.RegisterData;
import com.vetapp.data.models.register.RegisterResult;

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

    private static void register(RegisterData data, MutableLiveData<RegisterResult> result){
        RegisterDataSource ds = new RegisterDataSource();
        if(isEmailValid(data.getEmail()) && isPasswordValid(data.getPassword()))
            ds.register(data, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseAuth.getInstance().signOut();
                        // Sign in success, update UI with the signed-in user's information
                        Log.d( null,"createUserWithEmail:success");
                        result.setValue(new RegisterResult(true));
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(null, "createUserWithEmail:failure", task.getException());
                        result.setValue(new RegisterResult(false,task.getException().getMessage()));
                    }
                }
            });
    }
}
