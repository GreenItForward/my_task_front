package com.mytask.front.controller;

import com.mytask.front.utils.EPage;
import com.mytask.front.service.view.ScreenService;
import com.mytask.front.utils.EString;
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
        myTablesLabel.setText(EString.MY_TABS.getString());
        tableInfoLabel.setText(EString.INFORMATION_TAB.getString());
        descriptionLabel.setText(EString.DESCRIPTION.getString());
        backToMenuBtn.setText(EString.BACK_TO_MENU.getString());
        openTableBtn.setText(EString.OPEN_TABLE.getString());
        openTableBtn.setOnAction(event -> screenService.setScreen(EPage.SHOW_TAB));
        backToMenuBtn.setOnAction(event -> screenService.setScreen(EPage.INDEX));
    }

}