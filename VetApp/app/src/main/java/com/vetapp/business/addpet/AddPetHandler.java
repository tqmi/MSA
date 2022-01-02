package com.vetapp.business.addpet;

import com.vetapp.data.models.pet.Pet;
import com.vetapp.ui.main_page.client.activities.addpet.AddPetFormState;

public class AddPetHandler {
    public static AddPetFormState dataChanged(Pet pet){
        if(pet.getName() != null && pet.getCategory() != null && pet.getRace() != null)
            return new AddPetFormState(true);
        return new AddPetFormState(false);
    }
}
