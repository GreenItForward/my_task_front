package com.mytask.front.controller;

import com.mytask.front.service.view.ScreenService;
import com.mytask.front.service.view.UserService;
import com.mytask.front.utils.EPage;
import com.mytask.front.utils.EString;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ConnectionController {
    @FXML
    private TextField email, password;
    @FXML
    private Button sinscrire, seconnecter;
    @FXML
    private Label error;
    private ScreenService screenService;

    @FXML
    public void initialize() {
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
            String res = UserService.connectUser(email.getText(), password.getText());
            if(res.equals("ok")) {
                System.out.println(EString.SIGN_IN_IN_PROGRESS.toString());
                screenService.loadScreen(EPage.INDEX, IndexController::new);
                screenService.setScreen(EPage.INDEX);
            } else {
                error.setText(res);
            }
        });
    }
}