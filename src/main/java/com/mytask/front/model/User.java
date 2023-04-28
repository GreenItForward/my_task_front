package com.mytask.front.model;

import com.mytask.front.utils.EAuthEndpoint;

public class User {
    private String email;
    private String nom;
    private String prenom;
    private String password;
    private String token;

    public User() {
        this.email = "";
        this.nom = "";
        this.prenom = "";
        this.password = "";
        this.token = "";
    }

    public User(String email, String nom, String prenom, String password) {
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
        this.token = "";
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.nom = "";
        this.prenom = "";
        this.token = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String toJSON(String endpoint) {
        String result = "{\"email\":\"" + this.email + "\",\"password\":\"" + this.password;
        if (endpoint.equals("register")) {
            result += "\",\"name\":\"" + this.nom + "\",\"firstname\":\"" + this.prenom;
        }
        return result + "\"}";
    }


    public String toJSON(EAuthEndpoint endpoint) {
        String result = "{\"email\":\"" + this.email + "\",\"password\":\"" + this.password;
        if (endpoint == EAuthEndpoint.REGISTER) {
            result += "\",\"name\":\"" + this.nom + "\",\"firstname\":\"" + this.prenom;
        }
        return result + "\"}";
    }
}
