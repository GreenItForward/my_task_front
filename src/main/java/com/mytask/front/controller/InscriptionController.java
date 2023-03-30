package com.mytask.front.controller;

import com.mytask.front.service.ScreenService;
import com.mytask.front.utils.EPage;
import com.mytask.front.utils.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class InscriptionController {
    @FXML
    private TextField email, nom, prenom, password;
    @FXML
    private Button sinscrire, seconnecter;
    private final ScreenService screenService;
    private User user;

    public InscriptionController(ScreenService screenService) {
        this.screenService = screenService;
    }

    public void initialize() {
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