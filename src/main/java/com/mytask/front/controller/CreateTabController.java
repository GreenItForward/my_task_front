package com.mytask.front.controller;

import com.mytask.front.model.Project;
import com.mytask.front.service.api.impl.ProjectApiClient;
import com.mytask.front.utils.EPage;
import com.mytask.front.service.view.ScreenService;
import com.mytask.front.utils.EString;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateTabController {
    private ScreenService screenService;

    @FXML
    private Button joinTableBtn, createTableBtn, backToMenuBtn;

    @FXML
    private Label joinTableLabel, colorLabel, labelEtiquette, descriptionLabel, nameLabel;

    @FXML
    private TextField nameTextField, descriptionTextField, joinCodeTextField, labelTextField;



    @FXML
    public void initialize() {
        createTableBtn.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                screenService = ScreenService.getInstance((Stage) createTableBtn.getScene().getWindow());
            }
        });
        backToMenuBtn.setText(EString.BACK_TO_MENU.getString());
        joinTableBtn.setText(EString.JOIN_TAB.getString());
        joinTableLabel.setText(EString.LABEL_JOIN_TAB.getString());
        createTableBtn.setText(EString.CREATE_TAB.getString());
        nameTextField.setPromptText(EString.NAME_TAB.getString());
        descriptionTextField.setPromptText(EString.DESCRIPTION_TAB.getString());
        labelTextField.setPromptText("Frond-end");
        joinCodeTextField.setPromptText("Code du tableau");
        colorLabel.setText(EString.SELECT_COLOR.getString());
        labelEtiquette.setText(EString.LABEL.getString());
        descriptionLabel.setText(EString.DESCRIPTION.getString());
        nameLabel.setText(EString.NAME.getString());
        backToMenuBtn.setOnAction(event -> screenService.setScreen(EPage.INDEX));

        createTableBtn.setOnAction(event -> {
            ProjectApiClient projectApiClient = new ProjectApiClient();
            Project project = new Project();
            project.setNom(nameTextField.getText());
            project.setDescription(descriptionTextField.getText());
            project.setUserId(1);
            projectApiClient.createProject(project);
            screenService.loadScreen(EPage.SHOW_TAB, ShowTabController::new);
            screenService.setScreen(EPage.SHOW_TAB);
        });
    }

}