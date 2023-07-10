package com.mytask.front.controller;
import com.mytask.front.exception.AuthException;
import com.mytask.front.model.UserSettingModel;
import com.mytask.front.service.api.impl.UserSettingApiClient;
import com.mytask.front.service.view.UserService;
import com.mytask.front.utils.enums.EPage;
import com.mytask.front.service.view.ScreenService;
import com.mytask.front.utils.enums.EString;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class IndexController {
    @FXML
    private Text bienvenue;

    @FXML
    private Button voir_tableau;

    @FXML
    private Button creer_tableau;

    @FXML
    private Button se_deconnecter;

    @FXML
    private Button parametre_utilisateur;
    @FXML
    private Button quitter;

    @FXML
    private VBox rootBox;

    private ScreenService screenService;
    private UserSettingApiClient userSettingApiClient;
    private UserSettingModel userSettingModel;

    @FXML
    public void initialize() throws AuthException {
        userSettingApiClient = UserSettingApiClient.getInstance();
        try {
            userSettingModel = userSettingApiClient.getUserSettings();
        } catch (AuthException e) {
            throw new RuntimeException(e);
        }

        Platform.runLater(() -> {
            ParamController.getInstance().setBackgroundStyle(userSettingModel.getBackground());
            rootBox.setStyle(userSettingModel.getBackground());
        });

        bienvenue.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                screenService = ScreenService.getInstance((Stage) bienvenue.getScene().getWindow());
            }
        });
        bienvenue.setText(EString.WELCOME.toString() + UserService.getCurrentUser().getPrenom());
        voir_tableau.setOnAction(event -> {
            System.out.println(EString.SHOW_TAB_LOG.toString());
            screenService.setScreen(EPage.SHOW_ALL_TAB);
        });

        creer_tableau.setOnAction(event -> {
            System.out.println(EString.CREATE_TAB_LOG.toString());
            screenService.setScreen(EPage.CREATE_TAB);
        });

        se_deconnecter.setOnAction(event -> {
            System.out.println(EString.DISCONNECT_LOG.toString());
            screenService.setScreen(EPage.CONNECTION);
        });


        parametre_utilisateur.setOnAction(event -> {
            System.out.println(EString.PARAM_LOG.toString());
            screenService.setScreen(EPage.PARAM);
        });

        quitter.setOnAction(event -> Platform.exit());
    }
}