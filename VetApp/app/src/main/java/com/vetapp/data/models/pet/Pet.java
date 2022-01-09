package com.vetapp.data.models.pet;

import android.graphics.Bitmap;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.vetapp.data.models.appointment.Appointment;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Pet {
    private String name;
    private String category;
    private String race;
    private List<Appointment> appointments;
    @DocumentId
    private String docid;
    @Exclude private Bitmap image;

    public Pet() {
    }

    public Pet(String name, String category, String race) {
        this.name = name;
        this.category = category;
        this.race = race;
        appointments = new ArrayList<>();
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Exclude
    public Bitmap getImage() {
        return image;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    @Override
    public String toString() {
        return name;
    }
}
