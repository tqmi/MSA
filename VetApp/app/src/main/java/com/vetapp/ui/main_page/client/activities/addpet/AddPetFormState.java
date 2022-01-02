package com.vetapp.ui.main_page.client.activities.addpet;

import androidx.annotation.Nullable;

public class AddPetFormState {

    @Nullable
    private Integer error;
    private boolean isDataValid;

    public AddPetFormState(@Nullable Integer error) {
        this.error = error;
        this.isDataValid = false;
    }

    public AddPetFormState(boolean isDataValid) {
        this.error = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getError() {
        return error;
    }

    boolean isDataValid() {
        return isDataValid;
    }

}
