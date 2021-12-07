package com.vetapp.data.datasource.register;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.vetapp.data.models.register.RegisterData;

public class RegisterDataSource {
    private FirebaseAuth mAuth;

    public RegisterDataSource() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void register(RegisterData data, OnCompleteListener<AuthResult> callback){
        mAuth.createUserWithEmailAndPassword(data.getEmail(),data.getPassword()).addOnCompleteListener(callback);
    }
}
