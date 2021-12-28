package com.vetapp.data.models.user;

import com.vetapp.data.models.pet.Pet;

import java.util.ArrayList;
import java.util.List;

public class UserData {

    private String email;
    private String firstName;
    private String lastName;
    private UserType type;
    private String phone;
    private List<Pet> pets;


    public UserData() {
        this.pets = new ArrayList<>();
    }

    public UserData(String email, String firstName, String lastName, UserType type, String phone) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
        this.phone = phone;
        this.pets = new ArrayList<>();
    }

    public List<Pet> getPets(){
        return pets;
    }
    public void addPet(Pet pet){
        pets.add(pet);
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public UserType getType() {
        return type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }
}
