package com.vetapp.ui.addpet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vetapp.business.addpet.AddPetHandler;
import com.vetapp.data.models.pet.Pet;


public class AddPetViewModel extends ViewModel {
    private MutableLiveData<AddPetFormState> addPetFormState = new MutableLiveData<>();

    public LiveData<AddPetFormState> getAddPetFormState(){
        return addPetFormState;
    }

    public void registerDataChanged(Pet data){
        addPetFormState.setValue(AddPetHandler.dataChanged(data));
    }
}
