package com.vetapp.data.models.medication;

import com.google.firebase.Timestamp;
import com.vetapp.data.models.vet.Schedule;

import java.util.List;

public class Prescription {


    private Medicine medicine;
    private int quantity;
    private Timestamp endTime;
    private Timestamp startTime;
    private List<Schedule.TimePoint> dailyDose;

    public Prescription(Medicine medicine, int quantity, Timestamp endTime, Timestamp startTime, List<Schedule.TimePoint> dailyDose) {
        this.medicine = medicine;
        this.quantity = quantity;
        this.endTime = endTime;
        this.startTime = startTime;
        this.dailyDose = dailyDose;
    }

    public Prescription() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public List<Schedule.TimePoint> getDailyDose() {
        return dailyDose;
    }

    public void setDailyDose(List<Schedule.TimePoint> dailyDose) {
        this.dailyDose = dailyDose;
    }
}
