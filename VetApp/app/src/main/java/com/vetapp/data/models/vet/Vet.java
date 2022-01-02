package com.vetapp.data.models.vet;

import java.io.Serializable;

public class Vet implements Serializable {
    private String clinicName;
    private String address;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;

    public Vet() {
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getClinicName() {
        return clinicName;
    }

    public String getAddress() {
        return address;
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

    public String getPhone() {
        return phone;
    }
}
