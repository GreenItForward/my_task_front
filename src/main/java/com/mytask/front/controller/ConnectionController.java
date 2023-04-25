package com.mytask.front.controller;

import com.mytask.front.exception.AuthException;
import com.mytask.front.model.User;
import com.mytask.front.service.api.impl.AuthApiClient;
import com.mytask.front.service.view.ScreenService;
import com.mytask.front.service.view.UserService;
import com.mytask.front.utils.EPage;
import com.mytask.front.utils.EString;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
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
            User user = new User(email.getText(), password.getText());
            System.out.println(EString.SIGN_IN_IN_PROGRESS.toString());

            try {
                String token = authApiClient.authentify(user, "login");
                UserService.setCurrentUser(authApiClient.getUser(token));
                UserService.getCurrentUser().setToken(token);
                resetFields(null);
            } catch (AuthException e) {
                System.out.println(e.getMessage());
                error.setText(e.getMessage());
            }

            screenService.loadScreen(EPage.INDEX, IndexController::new);
            screenService.setScreen(EPage.INDEX);
        });

        activerToucheEntree(sinscrire, () -> sinscrire.fire());
        activerToucheEntree(seconnecter, () -> seconnecter.fire());

        gestionBoutons();
    }

    public static void activerToucheEntree(Button button, Runnable actionOnEntree) {
        button.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                actionOnEntree.run();
                e.consume();
            }
        });
    }

    private void gestionBoutons() {
        activerToucheEntree(seconnecter, this::seConnecter);
        activerToucheEntree(sinscrire, this::sInscrire);

        seconnecter.setDefaultButton(true);
    }

    private void sInscrire() {
        sinscrire.fire();
    }

    private void seConnecter() {
        seconnecter.fire();
    }

    private void resetFields(TextField textField) {
        if (textField != null) {
            textField.setText("");
            return;
        }

        email.setText("");
        password.setText("");
    }
}
