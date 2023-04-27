package com.mytask.front.service.api;

import com.mytask.front.model.Project;
import org.json.JSONException;

import java.util.ArrayList;

public interface ProjectApiClientInterface {
    void createProject(Project project);
    Project getProjectById(int id);
    ArrayList<Project> getProjectByUser() throws JSONException;
    void updateProject(Project project);
    void deleteProject(int id);
}
