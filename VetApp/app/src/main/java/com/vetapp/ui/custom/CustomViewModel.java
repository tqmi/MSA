package com.vetapp.ui.custom;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CustomViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public CustomViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("custom");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
