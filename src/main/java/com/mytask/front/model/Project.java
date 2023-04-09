package com.mytask.front.model;

public class Project {
    private String nom;
    private String description;
    private String codeJoin;
    private int userId;
   // private User user;

    public Project(String nom, String description, String codeJoin, int userId) {
        this.nom = nom;
        this.description = description;
        this.codeJoin = codeJoin;
        this.userId = userId;
    }

    public Project() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodeJoin() {
        return codeJoin;
    }

    public void setCodeJoin(String codeJoin) {
        this.codeJoin = codeJoin;
    }

    // transforme un objet Project en JSON
public String toJSON() {
        return "{\"nom\":\"" + this.nom + "\",\"description\":\"" + this.description + "\",\"codeJoin\":\"" + this.codeJoin + "\",\"userId\":\"" + this.userId + "\"}";
    }
}
