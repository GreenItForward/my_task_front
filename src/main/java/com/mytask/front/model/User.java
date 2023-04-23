package com.mytask.front.model;

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

    public String toJSON(boolean isLogin) {
        String result = "{\"email\":\"" + this.email + "\",\"password\":\"" + this.password;
        if (!isLogin) {
            result += "\",\"name\":\"" + this.nom;
        }
        return result + "\"}";
    }
}
