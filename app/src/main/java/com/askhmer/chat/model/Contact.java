package com.askhmer.chat.model;

/**
 * Created by Lincoln on 15/01/16.
 */
public class Contact {
    private String name, phoneNumber;

    public Contact() {
    }
    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String id) {
        this.phoneNumber = id;
    }
}
