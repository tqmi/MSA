package com.vetapp.ui.home.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vetapp.data.models.user.UserData;
import com.vetapp.data.persistent.user.UserState;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        UserData data = UserState.getCurrentUser().getData();
        mText.setValue("Hello " + data.getFirstName() + data.getLastName() + "\n" + "your number is " + data.getPhone());
    }

    public LiveData<String> getText() {
        return mText;
    }
}