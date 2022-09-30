package com.paavieira.campsite.reservations.framework.controllers.dtos;

import com.paavieira.campsite.reservations.framework.validations.ValidFullName;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@ValidFullName
public class User {

    @Size(min = 3, message = "First name is too short")
    private String firstName;

    @Size(min = 3, message = "Last name is too short")
    private String lastName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    public User() {
    }

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public static User fromDomain(com.paavieira.campsite.reservations.domain.User user) {
        return new User(user.getFirstName(), user.getLastName(), user.getEmail());
    }

    public com.paavieira.campsite.reservations.domain.User toDomain() {
        return new com.paavieira.campsite.reservations.domain.User(firstName, lastName, email);
    }
}