package com.mytask.front.controller;

import com.mytask.front.exception.AuthException;
import com.mytask.front.service.api.impl.AuthApiClient;
import com.mytask.front.service.view.ScreenService;
import com.mytask.front.utils.EPage;
import com.mytask.front.utils.EString;
import com.mytask.front.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class InscriptionController {
    @FXML
    private TextField email, nom, prenom;
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
        sinscrire.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                screenService = ScreenService.getInstance((Stage) sinscrire.getScene().getWindow());
            }
        });
        seconnecter.setOnAction(event -> {
            System.out.println(EString.CONNECTION.toString());
            screenService.setScreen(EPage.CONNECTION);
        });

        sinscrire.setOnAction(event -> {
            User user = new User(email.getText(), nom.getText(), prenom.getText(), password.getText());
            try {
                authApiClient.authentify(user, "register");
                System.out.println(EString.SIGN_UP_IN_PROGRESS.toString());
                screenService.loadScreen(EPage.INDEX, IndexController::new);
                screenService.setScreen(EPage.INDEX);
                resetFields(null);
            } catch (AuthException e) {
                System.out.println(e.getMessage());
                error.setText(e.getMessage());
            }
        });
    }

    private void resetFields(TextField textField) {
        if (textField != null) {
            textField.setText("");
            return;
        }

        email.setText("");
        nom.setText("");
        prenom.setText("");
        password.setText("");
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

        sinscrire.setDefaultButton(true);
    }

    private void sInscrire() {
        sinscrire.fire();
    }

    private void seConnecter() {
        seconnecter.fire();
    }
}