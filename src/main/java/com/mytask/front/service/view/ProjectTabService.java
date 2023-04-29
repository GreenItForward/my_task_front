package com.mytask.front.service.view;

import com.mytask.front.controller.ShowTabController;
import com.mytask.front.exception.AuthException;
import com.mytask.front.model.Project;
import com.mytask.front.model.Task;
import com.mytask.front.service.api.impl.TaskApiClient;
import com.mytask.front.utils.EPage;
import com.mytask.front.utils.EStatus;
import javafx.stage.Window;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ProjectTabService {
    private static ProjectTabService instance;
    private ScreenService screenService;

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
        screenService = ScreenService.getInstance(null);
        try {
            Project.setTasks(TaskApiClient.getInstance().getTasksByProject(project));
        } catch (JSONException | AuthException e) {
            throw new RuntimeException(e);
        }

        ShowTabController.getInstance().setProject(project);
        screenService.setScreen(EPage.SHOW_TAB);
    }

    public void closeCurrentPopup(Window window) {
       PopupService.getInstance().closeCurrentPopup(window);
    }
}