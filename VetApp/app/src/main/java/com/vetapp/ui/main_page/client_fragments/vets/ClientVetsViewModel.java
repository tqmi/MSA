package com.vetapp.ui.main_page.client_fragments.vets;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClientVetsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ClientVetsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("client vets fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}