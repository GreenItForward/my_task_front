package com.mytask.front.controller;

import com.mytask.front.exception.AuthException;
import com.mytask.front.model.UserSettingModel;
import com.mytask.front.service.api.impl.UserSettingApiClient;
import com.mytask.front.service.view.ScreenService;
import com.mytask.front.utils.enums.EPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ParamController {
    @FXML
    private ColorPicker colorPicker;

    @FXML
    private TextField backgroundImageURL;

    @FXML
    private ComboBox<String> resolutionPicker;

    @FXML
    private VBox rootBox;

    private Scene scene;

    @FXML
    private Button backToMenuBtn, applyButton;


    private static ParamController instance;
    private ScreenService screenService;
    private String background;

    private ParamController() { }

    public static ParamController getInstance() {
        if (instance == null) {
            instance = new ParamController();
        }
        return instance;
    }

    @FXML
    public void initialize() {
        backToMenuBtn.setOnAction(event -> screenService.setScreen(EPage.INDEX));
        applyButton.setOnAction(this::applySettings);

        applyButton.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                screenService = ScreenService.getInstance((Stage) applyButton.getScene().getWindow());
                scene = newValue;
            }
        });
    }

    public void applySettings(ActionEvent event) {
        Color color = colorPicker.getValue();
        String rgb = String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));


        if (rgb != null && !rgb.isEmpty()) {
            rootBox.setStyle("-fx-background-color: " + rgb + ";");
            background = "-fx-background-color: " + rgb + ";";
        }

        String imageURL = backgroundImageURL.getText();
        if (imageURL != null && !imageURL.isEmpty()) {
            rootBox.setStyle(rootBox.getStyle() + "-fx-background-image: url('" + imageURL + "');");
            background = "-fx-background-image: url('" + imageURL + "');";
        }

        try {
            UserSettingApiClient.getInstance().postUserSetting(new UserSettingModel(background));
        } catch (AuthException e) {
            throw new RuntimeException(e);
        }

        try {
            UserSettingModel setting = UserSettingApiClient.getInstance().getUserSettings();
        } catch (AuthException e) {
            throw new RuntimeException(e);
        }

    }

    public String getBackgroundStyle() {
        return background;
    }

    public void setBackgroundStyle(String background) {
        this.background = background;
        rootBox.setStyle(background);
    }

}
