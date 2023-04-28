package com.mytask.front.service.view;

import com.mytask.front.model.Project;
import com.mytask.front.utils.EPage;
import javafx.stage.Window;

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
        screenService.setScreen(EPage.SHOW_TAB); // A CHANGER
        //TODO: Open project in new window
    }

    public void closeCurrentPopup(Window window) {
       PopupService.getInstance().closeCurrentPopup(window);
    }
}