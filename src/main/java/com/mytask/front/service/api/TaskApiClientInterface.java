package com.mytask.front.service.api;

import com.mytask.front.exception.AuthException;
import com.mytask.front.model.Project;
import com.mytask.front.model.Task;
import org.json.JSONException;

import java.util.List;

public interface TaskApiClientInterface {
    Task createTask(Task project) throws JSONException, AuthException;
    Task getTaskById(int id);
    void updateTask(Task project, Integer userId);
    List<Task> getTasksByProject(Project project) throws JSONException, AuthException;

    void exportTasksToPdf(Project project);

    void exportTasksToCsv(Project project);

    void exportTasksToJson(Project project);

    void deleteTask(Task task);
}
