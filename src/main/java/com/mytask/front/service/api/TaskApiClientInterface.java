package com.mytask.front.service.api;

import com.mytask.front.model.Project;
import com.mytask.front.model.Task;
import org.json.JSONException;

import java.util.List;

public interface TaskApiClientInterface {
    void createTask(Task project);
    Task getTaskById(int id);
    void updateTask(Task project);
    void deleteTask(int id);
    List<Task> getTasksByProject(Project project) throws JSONException;
}
