package com.mytask.front.utils;

public class User {
    private String email;
    private String nom;
    private String prenom;
    private String password;

    public User() {
        this.email = "";
        this.nom = "";
        this.prenom = "";
        this.password = "";
    }

    public User(String email, String nom, String prenom, String password) {
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
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
}
