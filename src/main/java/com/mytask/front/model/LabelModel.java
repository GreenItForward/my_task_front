package com.mytask.front.model;

public class Label {
    private String nom;
    private String couleur;

    private int taskId;
    private int projectId;

    public Label(String nom, String couleur) {
        this.nom = nom;
        this.couleur = couleur;
        this.taskId = 0;
        this.projectId = 0;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
