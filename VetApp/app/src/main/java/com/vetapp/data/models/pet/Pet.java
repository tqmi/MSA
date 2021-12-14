package com.vetapp.data.models.pet;

import androidx.annotation.Nullable;

public class Pet {
    private String name;
    private String category;
    private String race;
    @Nullable
    private String picture_url;
    private String id;


    public Pet() {
    }

    public Pet(String id,String name, String category, String race,@Nullable String picture_url) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.race = race;
        this.picture_url = picture_url;
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

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
