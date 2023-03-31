package com.mytask.front.controller;

import com.mytask.front.utils.EPage;
import com.mytask.front.service.ScreenService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ShowAllTabController {

    @FXML
    private Label myTablesLabel, tableInfoLabel, descriptionLabel;

    @FXML
    private Button backToMenuBtn, openTableBtn;

    private ScreenService screenService;

    @FXML
    public void initialize() {
        backToMenuBtn.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                screenService = ScreenService.getInstance((Stage) backToMenuBtn.getScene().getWindow());
            }
        });
        myTablesLabel.setText("Mes tableaux");
        tableInfoLabel.setText("Informations du tableau");
        descriptionLabel.setText("Description");
        backToMenuBtn.setText("Retour au menu");
        openTableBtn.setText("Ouvrir le tableau");
        openTableBtn.setOnAction(event -> screenService.setScreen(EPage.SHOW_TAB));
        backToMenuBtn.setOnAction(event -> screenService.setScreen(EPage.INDEX));


    }

}