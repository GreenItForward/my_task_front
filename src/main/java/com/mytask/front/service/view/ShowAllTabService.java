package com.mytask.front.service.view;

import com.mytask.front.model.Project;

import java.util.ArrayList;
import java.util.List;

public class ShowAllTabService {
    private static ArrayList<Project> projects;
    private static ShowAllTabService instance;


    private ShowAllTabService() {
    }

    public static ShowAllTabService getInstance() {
        if (instance == null) {
            instance = new ShowAllTabService();
            projects = new ArrayList<>();
        }
        return instance;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        ShowAllTabService.projects = projects;
    }

    public void addProject(Project project) {
        projects.add(project);
    }

    public void removeProject(Project project) {
        projects.remove(project);
    }

    public void updateProject(Project project) {
        for (Project p : projects) {
            if (p.getId() == project.getId()) {
                p.setNom(project.getNom());
                p.setDescription(project.getDescription());
                p.setCodeJoin(project.getCodeJoin());
                p.setUserId(project.getUserId());
                p.setLabels(project.getLabels());
            }
        }
    }

}
