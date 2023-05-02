package com.mytask.front.model;

import java.util.List;

public class Project {
    private int id;
    private String nom;
    private String description;
    private String codeJoin;
    private int userId;
   // private User user;
    private static List<Task> tasks;
   private List<LabelModel> labels;

    public Project(String nom, String description) {
        this.id = 0;
        this.nom = nom;
        this.description = description;
        this.codeJoin = "";
        this.userId = 1;
    }

    public Project() {
    }

    public Project(String nom, String description, String codeJoin, int id) {
        this.nom = nom;
        this.description = description;
        this.codeJoin = codeJoin;
        this.id = id;
    }
    public Project(String nom, String description, String codeJoin, int id, int userId) {
        this.nom = nom;
        this.description = description;
        this.codeJoin = codeJoin;
        this.id = id;
        this.userId = userId;
    }

    public static void setTasks(List<Task> tasksByProject) {
        tasks = tasksByProject;
    }

    public static List<Task> getTasks() {
        return tasks;
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

    public List<LabelModel> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelModel> labels) {
        this.labels = labels;
    }

    public void addLabel(LabelModel label) {
        this.labels.add(label);
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    // transforme un objet Project en JSON
    public String toJSON() {
        return "{\"nom\":\"" + this.nom + "\",\"description\":\"" + this.description + "\",\"codeJoin\":\"" + this.codeJoin + "\",\"userId\":\"" + this.userId + "\"}";
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
    }
}
