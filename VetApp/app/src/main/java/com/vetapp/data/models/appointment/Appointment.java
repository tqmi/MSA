package com.vetapp.data.models.appointment;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.vetapp.data.models.vet.Schedule;
import com.vetapp.data.models.vet.VisitType;

public class Appointment {

    private String petName;
    private String ownerName;
    private String petId;
    private String ownerId;
    private VisitType visitType;
    private Timestamp date;
    @Exclude
    private Schedule.TimeSlot timeSlot;

    public Appointment() {
    }

    public Appointment(String petName, String ownerName, String petId, String ownerId, VisitType visitType, Timestamp date, Schedule.TimeSlot timeSlot) {
        this.petName = petName;
        this.ownerName = ownerName;
        this.petId = petId;
        this.ownerId = ownerId;
        this.visitType = visitType;
        this.date = date;
        this.timeSlot = timeSlot;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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
                "petName='" + petName + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", petId='" + petId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", visitType=" + visitType +
                ", date=" + date +
                ", timeSlot=" + timeSlot +
                '}';
    }
}
