package com.mytask.front.controller;

import com.mytask.front.service.ScreenService;
import com.mytask.front.utils.EPage;
import com.mytask.front.utils.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ConnectionController {
    @FXML
    private TextField email, nom, prenom, password;
    @FXML
    private Button sinscrire, seconnecter;
    private ScreenService screenService;
    private User user;

    @FXML
    public void initialize() {
        seconnecter.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                screenService = ScreenService.getInstance((Stage) newValue.getWindow());
            }
        });

        sinscrire.setOnAction(event -> {
            System.out.println("Inscription");
            screenService.setScreen(EPage.INSCRIPTION);
        });

        seconnecter.setOnAction(event -> {
            user = new User(email.getText(), nom.getText(), prenom.getText(), password.getText());
            System.out.println("Connection de l'utilisateur...");
            screenService.setScreen(EPage.INDEX);
        });
    }
}