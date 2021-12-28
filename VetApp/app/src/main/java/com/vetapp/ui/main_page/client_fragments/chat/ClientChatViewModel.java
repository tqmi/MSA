package com.vetapp.ui.main_page.client_fragments.chat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClientChatViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ClientChatViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("client chat fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}