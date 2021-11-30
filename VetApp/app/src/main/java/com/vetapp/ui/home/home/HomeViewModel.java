package com.vetapp.ui.home.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vetapp.data.persistent.user.UserState;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(UserState.getCurrentUser().getEmail());
    }

    public LiveData<String> getText() {
        return mText;
    }
}