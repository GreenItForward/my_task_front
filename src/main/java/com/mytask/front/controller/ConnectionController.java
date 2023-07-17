package com.mytask.front.controller;

import com.mytask.front.exception.AuthException;
import com.mytask.front.model.User;
import com.mytask.front.service.api.impl.AuthApiClient;
import com.mytask.front.service.view.ScreenService;
import com.mytask.front.service.view.UserService;
import com.mytask.front.utils.enums.EAuthEndpoint;
import com.mytask.front.utils.enums.EPage;
import com.mytask.front.utils.enums.EString;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import static com.mytask.front.service.AppService.activerToucheEntree;


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
            System.out.println(EString.SIGN_UP);
            screenService.setScreen(EPage.INSCRIPTION);
        });


        seconnecter.setOnAction(event -> {
            User user = new User(email.getText(), password.getText());
            System.out.println(EString.SIGN_IN_IN_PROGRESS);

            String token = null;
            try {
                token = authApiClient.authentify(user, EAuthEndpoint.LOGIN);
                UserService.setCurrentUser(authApiClient.getUser(token));
                UserService.getCurrentUser().setToken(token);
                resetFields(null);

                if (token.equals(UserService.getCurrentUser().getToken())) {
                    screenService.loadScreen(EPage.INDEX, IndexController::new);
                    screenService.setScreen(EPage.INDEX);
                    screenService.loadScreen(EPage.SHOW_ALL_TAB, ShowAllTabController::getInstance);
                    screenService.loadScreen(EPage.SHOW_TAB, ShowTabController::getInstance);
                    screenService.loadScreen(EPage.PARAM, ParamController::getInstance);
                }
            } catch (AuthException e) {
                error.setText(e.getMessage());
            }
        });


        activerToucheEntree(sinscrire, () -> sinscrire.fire());
        activerToucheEntree(seconnecter, () -> seconnecter.fire());

        gestionBoutons();
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
