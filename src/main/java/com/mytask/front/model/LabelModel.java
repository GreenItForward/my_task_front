package com.mytask.front.model;

import javafx.scene.paint.Color;

import static com.mytask.front.service.AppService.colorToHexString;

public class LabelModel {
    private String nom;
    private Color couleur;
    private int projectId;

    public LabelModel(String nom, Color couleur) {
        this.nom = nom;
        this.couleur = couleur;
        this.projectId = 0;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Color getCouleur() {
        return couleur;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String toString() {
        return String.format("LabelModel {nom: %s, couleur: %s, projectId: %d}", nom, colorToHexString(couleur), projectId);
    }

    public String toJSON() {
        return "{\"nom\":\"" + this.nom + "\",\"couleur\":\"" + colorToHexString(this.couleur) + "\",\"projectId\":\"" + this.projectId + "\"}";
    }
}
