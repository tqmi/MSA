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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.vetapp.R;
import com.vetapp.business.register.RegisterHandler;
import com.vetapp.data.models.register.RegisterData;
import com.vetapp.data.models.register.RegisterResult;
import com.vetapp.data.models.user.UserType;
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

        final EditText etEmail = binding.etEmail;
        final EditText etPassword = binding.etPassword;
        final EditText etFirstName = binding.etFirstName;
        final EditText etLastName = binding.etLastName;
        final EditText etPhone = binding.etPhone;
        final RadioGroup rgChoseType = binding.rgChoseType;
        final Button registerButton = binding.register;
        final RadioButton rbVet = binding.rgVet;
        final RadioButton rbClient = binding.rgClient;



        registerViewModel.getRegisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(RegisterFormState registerFormState) {
                if (registerFormState == null) {
                    return;
                }
                registerButton.setEnabled(registerFormState.isDataValid());
                if (registerFormState.getUsernameError() != null) {
                    etEmail.setError(getString(registerFormState.getUsernameError()));
                }
                if (registerFormState.getPasswordError() != null) {
                    etPassword.setError(getString(registerFormState.getPasswordError()));
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
                registerData.setEmail(etEmail.getText().toString());
                registerData.setPassword(etPassword.getText().toString());
                registerData.setFirstName(etFirstName.getText().toString());
                registerData.setLastName(etLastName.getText().toString());
                registerData.setPhone(etPhone.getText().toString());
                registerViewModel.registerDataChanged(registerData);
            }
        };
        etEmail.addTextChangedListener(afterTextChangedListener);
        etPassword.addTextChangedListener(afterTextChangedListener);
        etFirstName.addTextChangedListener(afterTextChangedListener);
        etLastName.addTextChangedListener(afterTextChangedListener);
        etPhone.addTextChangedListener(afterTextChangedListener);


        rbClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rbVet.isChecked()){
                    registerData.setType(UserType.VET);
                    registerViewModel.registerDataChanged(registerData);
                }
            }
        });
        rbVet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rbVet.isChecked()){
                    registerData.setType(UserType.VET);
                    registerViewModel.registerDataChanged(registerData);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterHandler.register(registerData,registerResult);
            }
        });


    }


}