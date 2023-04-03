package com.mytask.front.controller;

import com.mytask.front.service.ScreenService;
import com.mytask.front.service.UserService;
import com.mytask.front.utils.EPage;
import com.mytask.front.utils.EString;
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

    @FXML
    public void initialize() {
        sinscrire.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                screenService = ScreenService.getInstance((Stage) sinscrire.getScene().getWindow());
            }
        });
        seconnecter.setOnAction(event -> {
            System.out.println(EString.CONNECTION.getString());
            screenService.setScreen(EPage.CONNECTION);
        });

        sinscrire.setOnAction(event -> {
            User user = new User(email.getText(), nom.getText(), prenom.getText(), password.getText());
            if(UserService.signUpUser(user)) {
                System.out.println(EString.SIGN_UP_IN_PROGRESS.getString());
                screenService.loadScreen(EPage.INDEX, IndexController::new);
                screenService.setScreen(EPage.INDEX);
            }
        });
    }
}