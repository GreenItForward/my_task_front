package com.mytask.front.service.view;

import com.mytask.front.controller.ShowTabController;
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
        // TODO: Décommenter pour utiliser l'API
/*
        try {
            Project.setTasks(TaskApiClient.getInstance().getTasksByProject(project));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
*/
        // TODO: supprimer ce bloc pour utiliser l'API
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Task task = new Task(i, "Tache-"+i, "Ceci est un mock de task ;)", project);
            if (i % 2 == 0) {
                task.setStatus(EStatus.DONE);
            } else if (i % 3 == 0) {
                task.setStatus(EStatus.IN_PROGRESS);
            } else {
                task.setStatus(EStatus.TODO);
            }
            tasks.add(task);
        }

        Project.setTasks(tasks);
        ShowTabController.getInstance().setProject(project);
        screenService.setScreen(EPage.SHOW_TAB); // A CHANGER
    }

    public void closeCurrentPopup(Window window) {
       PopupService.getInstance().closeCurrentPopup(window);
    }
}