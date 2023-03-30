package com.mytask.front.controller;

import com.mytask.front.service.ScreenService;
import com.mytask.front.utils.EPage;
import com.mytask.front.utils.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ConnectionController {
    @FXML
    private TextField email, nom, prenom, password;
    @FXML
    private Button sinscrire, seconnecter;
    private final ScreenService screenService;
    private User user;

    public ConnectionController(ScreenService screenService) {
        this.screenService = screenService;
    }

    public void initialize() {
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