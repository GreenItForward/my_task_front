package com.mytask.front.service.api;

import com.mytask.front.model.Project;

public interface ProjectApiClientInterface {
    void createProject(Project project);
    Project getProjectById(int id);
    void updateProject(Project project);
    void deleteProject(int id);
}
