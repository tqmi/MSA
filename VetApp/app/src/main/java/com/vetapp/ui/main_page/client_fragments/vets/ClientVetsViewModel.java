package com.vetapp.ui.main_page.client_fragments.vets;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.vetapp.data.datasource.user.VetDataSource;
import com.vetapp.data.models.vet.Vet;

import java.util.ArrayList;
import java.util.List;

public class ClientVetsViewModel extends ViewModel {

    private MutableLiveData<List<Vet>> mVets;

    public ClientVetsViewModel() {
        mVets = new MutableLiveData<>(new ArrayList<Vet>());
        VetDataSource.setChangeListenerVets(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<Vet> vets = value.toObjects(Vet.class);

                Log.d("db", "called");
                mVets.setValue(vets);
            }
        });
    }

    public LiveData<List<Vet>> getVets() {
        return mVets;
    }
}