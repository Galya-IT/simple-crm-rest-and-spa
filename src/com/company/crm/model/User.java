package com.company.crm.model;

import com.company.crm.exceptions.UserPropertyValidationException;

public class User {
    private int id = 0;
    private String username = null;
    private String password = null;

    public User(String name, String password) throws UserPropertyValidationException {
        setName(name);
        setPassword(password);
    }

    public User(String name, String password, int id) throws UserPropertyValidationException {
        this(name, password);
        this.id = id;
    }

    public void setId(String idAsString) {
        int id = Integer.parseInt(idAsString);
        this.id = id;
    }

    public void setName(String name) throws UserPropertyValidationException {
        if (name.length() <= 50) {
            this.username = name;
        } else {
            throw new UserPropertyValidationException("The username length is incorrect.");
        }
    }

    public void setPassword(String pass) throws UserPropertyValidationException {
        if (pass.length() <= 50) {
            this.password = pass;
        } else {
            throw new UserPropertyValidationException("The password length is incorrect.");
        }
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public String toString() {
        String result = "";

        if (this.id > 0) {
            result += "Id: " + this.id + ", ";
        }

        if (this.username != null) {
            result += "Name: " + this.username;
        }

        result += System.getProperty("line.separator");
        return result;
    }
}
