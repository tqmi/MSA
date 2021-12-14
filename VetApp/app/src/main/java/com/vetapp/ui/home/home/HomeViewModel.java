package com.vetapp.ui.home.home;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.vetapp.data.datasource.pet.PetDataSource;
import com.vetapp.data.models.pet.Pet;
import com.vetapp.data.models.user.UserData;
import com.vetapp.data.persistent.user.UserState;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Pet>> mPets;

    public HomeViewModel() {
        mPets = new MutableLiveData<>(new ArrayList<Pet>());

        PetDataSource.setChangeListenerPets(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<Pet> pets = value.toObjects(Pet.class);

                UserState.getCurrentUser().getData().setPets(pets);

                mPets.setValue(pets);
            }
        });
    }

    public LiveData<List<Pet>> getPets() {
        return mPets;
    }
}