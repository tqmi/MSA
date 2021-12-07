package com.vetapp.data.models.user;

public class UserData {

    private String email;
    private String firstName;
    private String lastName;
    private UserType type;
    private String phone;

    public UserData() {
    }

    public UserData(String email, String firstName, String lastName, UserType type, String phone) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
        this.phone = phone;
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
}
