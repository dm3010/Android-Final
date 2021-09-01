package com.edu0988.phonebook.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    private String name;
    private String lastname;
    private String phone;
    private final String uuid;

    public User() {
        this.uuid = UUID.randomUUID().toString();
    }

    public User(String uuid) { this.uuid = uuid; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUuid() { return uuid; }

    @NonNull
    @Override
    public String toString() {
        return getName() + " " + getLastname();
    }
}
