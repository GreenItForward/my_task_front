package com.mytask.front.controller;

import com.mytask.front.service.view.ScreenService;
import com.mytask.front.service.view.UserService;
import com.mytask.front.utils.EPage;
import com.mytask.front.utils.EString;
import com.mytask.front.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class InscriptionController {
    @FXML
    private TextField email, nom, prenom, password;
    @FXML
    private Button sinscrire, seconnecter;
    @FXML
    private Label error;
    private ScreenService screenService;

    @FXML
    public void initialize() {
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
            String res = UserService.signUpUser(user);
            if(res.equals("ok")) {
                System.out.println(EString.SIGN_UP_IN_PROGRESS.toString());
                screenService.loadScreen(EPage.INDEX, IndexController::new);
                screenService.setScreen(EPage.INDEX);
            }
            else {
                error.setText(res);
            }
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

        sinscrire.setDefaultButton(true);
    }

    private void sInscrire() {
        sinscrire.fire();
    }

    private void seConnecter() {
        seconnecter.fire();
    }
}
