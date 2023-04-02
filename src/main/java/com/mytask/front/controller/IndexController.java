package com.mytask.front.controller;
import com.mytask.front.utils.EPage;
import com.mytask.front.service.ScreenService;
import com.mytask.front.utils.EString;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class IndexController {
    @FXML
    private Text bienvenue;

    @FXML
    private Button voir_tableau;

    @FXML
    private Button creer_tableau;

    @FXML
    private Button quitter;

    private ScreenService screenService;

    @FXML
    public void initialize() {
        bienvenue.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                screenService = ScreenService.getInstance((Stage) bienvenue.getScene().getWindow());
            }
        });
        bienvenue.setText(EString.WELCOME.getString() + "Ronan");

        voir_tableau.setOnAction(event -> {
            System.out.println(EString.SHOW_TAB_LOG.getString());
            screenService.setScreen(EPage.SHOW_ALL_TAB);
        });

        creer_tableau.setOnAction(event -> {
            System.out.println(EString.CREATE_TAB_LOG.getString());
           screenService.setScreen(EPage.CREATE_TAB);
        });

        quitter.setOnAction(event -> Platform.exit());
    }
}