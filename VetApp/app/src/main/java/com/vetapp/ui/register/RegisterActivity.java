package com.vetapp.ui.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vetapp.R;
import com.vetapp.business.register.RegisterHandler;
import com.vetapp.data.models.register.RegisterData;
import com.vetapp.data.models.register.RegisterResult;
import com.vetapp.databinding.ActivityLoginBinding;
import com.vetapp.databinding.ActivityRegisterBinding;
import com.vetapp.ui.login.LoginActivity;
import com.vetapp.ui.login.LoginViewModel;
import com.vetapp.ui.login.LoginViewModelFactory;

public class RegisterActivity extends AppCompatActivity {


    private RegisterViewModel registerViewModel;
    private ActivityRegisterBinding binding;
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private RegisterData registerData = new RegisterData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerViewModel = new ViewModelProvider(this, new RegisterViewModelFactory())
                .get(RegisterViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button registerButton = binding.register;

        registerViewModel.getRegisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(RegisterFormState registerFormState) {
                if (registerFormState == null) {
                    return;
                }
                registerButton.setEnabled(registerFormState.isDataValid());
                if (registerFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(registerFormState.getUsernameError()));
                }
                if (registerFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(registerFormState.getPasswordError()));
                }
            }
        });

        registerResult.observe(this, new Observer<RegisterResult>() {
            @Override
            public void onChanged(RegisterResult registerResult) {
                if(registerResult == null)
                    return;

                if(!registerResult.isSuccess()){
                    Toast.makeText(getApplicationContext(), registerResult.getErrmsg(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                registerData.setEmail(usernameEditText.getText().toString());
                registerData.setPassword(passwordEditText.getText().toString());
                registerViewModel.registerDataChanged(registerData);
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterHandler.register(registerData,registerResult);
            }
        });


    }


}