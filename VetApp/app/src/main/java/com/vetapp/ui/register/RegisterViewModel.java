package com.vetapp.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vetapp.business.register.RegisterHandler;
import com.vetapp.data.models.register.RegisterData;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();

    public LiveData<RegisterFormState> getRegisterFormState(){
        return registerFormState;
    }

    public void registerDataChanged(RegisterData data){
        registerFormState.setValue(RegisterHandler.registerDataChanged(data));
    }

}
