package com.mytask.front.service.view;

import com.mytask.front.model.Project;

public class ProjectTabService {
    private static ProjectTabService instance;

    private ProjectTabService() {
    }

    public static ProjectTabService getInstance() {
        if (instance == null) {
            instance = new ProjectTabService();
        }
        return instance;
    }

    public void openProject(Project project) {
        System.out.println("Ouverture du projet " + project.getNom());
        //TODO: Open project in new window
    }
}