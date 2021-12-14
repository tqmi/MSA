package com.vetapp.data.models.pet;

public class Pet {
    private String name;
    private String category;
    private String race;
    private String picture_url;

    public Pet() {
    }

    public Pet(String name, String category, String race, String picture_url) {
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
}
