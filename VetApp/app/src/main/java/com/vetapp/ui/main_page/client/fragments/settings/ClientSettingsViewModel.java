package com.vetapp.ui.main_page.client.fragments.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClientSettingsViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public ClientSettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("client settings fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
