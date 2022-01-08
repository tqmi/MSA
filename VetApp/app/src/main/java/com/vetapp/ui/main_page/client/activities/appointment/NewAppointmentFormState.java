package com.vetapp.ui.main_page.client.activities.appointment;

import androidx.annotation.Nullable;

public class NewAppointmentFormState {

    @Nullable
    private Integer petError;
    @Nullable
    private Integer visitTypeError;
    @Nullable
    private Integer dateError;
    @Nullable
    private Integer timeSlotError;

    private boolean dataValid = false;


    public NewAppointmentFormState(@Nullable Integer petError, @Nullable Integer visitTypeError, @Nullable Integer dateError, @Nullable Integer timeSlotError) {
        this.petError = petError;
        this.visitTypeError = visitTypeError;
        this.dateError = dateError;
        this.timeSlotError = timeSlotError;
    }

    public NewAppointmentFormState(boolean dataValid) {
        this.dataValid = dataValid;
    }

    public boolean isDataValid() {
        return dataValid;
    }

    @Nullable
    public Integer getPetError() {
        return petError;
    }

    @Nullable
    public Integer getVisitTypeError() {
        return visitTypeError;
    }

    @Nullable
    public Integer getDateError() {
        return dateError;
    }

    @Nullable
    public Integer getTimeSlotError() {
        return timeSlotError;
    }
}
