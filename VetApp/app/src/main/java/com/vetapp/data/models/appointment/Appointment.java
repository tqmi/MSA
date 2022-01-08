package com.vetapp.data.models.appointment;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.vetapp.data.models.pet.Pet;
import com.vetapp.data.models.vet.Schedule;
import com.vetapp.data.models.vet.VisitType;

public class Appointment {

    private Pet pet;
    private VisitType visitType;
    private Timestamp date;
    @Exclude
    private Schedule.TimeSlot timeSlot;

    public Appointment() {
    }

    public Appointment(Pet pet, VisitType visitType, Timestamp date, Schedule.TimeSlot timeSlot) {
        this.pet = pet;
        this.visitType = visitType;
        this.date = date;
        this.timeSlot = timeSlot;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public VisitType getVisitType() {
        return visitType;
    }

    public void setVisitType(VisitType visitType) {
        this.visitType = visitType;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Exclude
    public Schedule.TimeSlot getTimeSlot() {
        return timeSlot;
    }

    @Exclude
    public void setTimeSlot(Schedule.TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "pet=" + pet +
                ", visitType=" + visitType +
                ", date=" + date +
                ", timeSlot=" + timeSlot +
                '}';
    }
}
