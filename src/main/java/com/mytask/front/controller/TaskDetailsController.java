package com.mytask.front.controller;
import com.mytask.front.utils.EPage;
import com.mytask.front.service.ScreenService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class TaskDetailsController {
    @FXML
    private Text bienvenue;

    @FXML
    private Button voir_tableau;

    @FXML
    private Button creer_tableau;

    @FXML
    private Button quitter;

    private final ScreenService screenService;

    public TaskDetailsController(ScreenService screenService) {
        this.screenService = screenService;
    }

    public void initialize() {
        bienvenue.setText("Bienvenue Ronan");

        voir_tableau.setOnAction(event -> {
            System.out.println("Voir tableau");
            screenService.setScreen(EPage.SHOW_ALL_TAB);
        });

        creer_tableau.setOnAction(event -> {
            System.out.println("Creer tableau");
            screenService.setScreen(EPage.CREATE_TAB);
        });

        quitter.setOnAction(event -> {
            Platform.exit();
        });
    }
}
