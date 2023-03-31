package com.mytask.front.controller;

import com.mytask.front.service.ScreenService;
import com.mytask.front.utils.EPage;
import com.mytask.front.utils.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InscriptionController {
    @FXML
    private TextField email, nom, prenom, password;
    @FXML
    private Button sinscrire, seconnecter;
    private ScreenService screenService;
    private User user;

    @FXML
    public void initialize() {
        sinscrire.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                screenService = ScreenService.getInstance((Stage) sinscrire.getScene().getWindow());
            }
        });
        seconnecter.setOnAction(event -> {
            System.out.println("Connexion");
            screenService.setScreen(EPage.CONNECTION);
        });

        sinscrire.setOnAction(event -> {
            user = new User(email.getText(), nom.getText(), prenom.getText(), password.getText());
            System.out.println("Inscription de l'utilisateur...");
            screenService.setScreen(EPage.INDEX);
        });
    }
}