package com.vetapp.ui.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.vetapp.R;
import com.vetapp.databinding.ActivityLoginBinding;
import com.vetapp.databinding.ActivityRegisterBinding;
import com.vetapp.ui.login.LoginViewModel;
import com.vetapp.ui.login.LoginViewModelFactory;

public class RegisterActivity extends AppCompatActivity {


    private RegisterViewModel registerViewModel;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerViewModel = new ViewModelProvider(this, new RegisterViewModelFactory())
                .get(RegisterViewModel.class);


    }
}