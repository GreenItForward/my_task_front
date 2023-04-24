package com.mytask.front.controller;

import com.mytask.front.exception.AuthException;
import com.mytask.front.model.User;
import com.mytask.front.service.api.impl.AuthApiClient;
import com.mytask.front.service.view.ScreenService;
import com.mytask.front.utils.EPage;
import com.mytask.front.utils.EString;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ConnectionController {
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Button sinscrire, seconnecter;
    @FXML
    private Label error;
    private ScreenService screenService;
    private AuthApiClient authApiClient;

    @FXML
    public void initialize() {
        authApiClient = AuthApiClient.getInstance();
        seconnecter.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                screenService = ScreenService.getInstance((Stage) newValue.getWindow());
            }
        });

        sinscrire.setOnAction(event -> {
            System.out.println(EString.SIGN_UP.toString());
            screenService.setScreen(EPage.INSCRIPTION);
        });

        seconnecter.setOnAction(event -> {
            try {
                User user = new User(email.getText(), password.getText());
                authApiClient.authentify(user, "login");
                System.out.println(EString.SIGN_IN_IN_PROGRESS.toString());
                screenService.loadScreen(EPage.INDEX, IndexController::new);
                screenService.setScreen(EPage.INDEX);
            } catch (AuthException e) {
                System.out.println(e.getMessage());
                error.setText(e.getMessage());
            }
        });
    }
}