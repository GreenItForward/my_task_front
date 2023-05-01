package com.mytask.front.controller;

import com.mytask.front.utils.EAuthEndpoint;
import com.mytask.front.exception.AuthException;
import com.mytask.front.service.api.impl.AuthApiClient;
import com.mytask.front.service.view.ScreenService;
import com.mytask.front.service.view.UserService;
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
            System.out.println(EString.CONNECTION);
            screenService.setScreen(EPage.CONNECTION);
        });

        sinscrire.setOnAction(event -> {
            User user = new User(email.getText(), nom.getText(), prenom.getText(), password.getText());
            System.out.println(EString.SIGN_UP_IN_PROGRESS);

            String token = null;
            try {
                token = authApiClient.authentify(user, EAuthEndpoint.REGISTER);
                UserService.setCurrentUser(authApiClient.getUser(token));
                UserService.getCurrentUser().setToken(token);
                resetFields(null);

                if (token.equals(UserService.getCurrentUser().getToken())) {
                    screenService.loadScreen(EPage.INDEX, IndexController::new);
                    screenService.setScreen(EPage.INDEX);
                    screenService.loadScreen(EPage.SHOW_ALL_TAB, ShowAllTabController::getInstance);
                    screenService.loadScreen(EPage.SHOW_TAB, ShowTabController::getInstance);
                }
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

    public void activerToucheEntree(Button button, Runnable actionOnEntree) {
        button.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                actionOnEntree.run();
                e.consume();
            }
        });

        activerToucheEntree(sinscrire, () -> sinscrire.fire());
        activerToucheEntree(seconnecter, () -> seconnecter.fire());

        gestionBoutons();
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
