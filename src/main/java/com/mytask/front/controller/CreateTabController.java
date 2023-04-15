package com.mytask.front.controller;

import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Project;
import com.mytask.front.service.api.impl.LabelApiClient;
import com.mytask.front.service.api.impl.ProjectApiClient;
import com.mytask.front.utils.EPage;
import com.mytask.front.service.view.ScreenService;
import com.mytask.front.utils.EString;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class CreateTabController {
    private ScreenService screenService;

    @FXML
    private Button joinTableBtn, createTableBtn, backToMenuBtn, addLabelBtn;

    @FXML
    private Label joinTableLabel, colorLabel, labelEtiquette, descriptionLabel, nameLabel;

    @FXML
    private TextField nameTextField, descriptionTextField, joinCodeTextField, labelTextField;

    @FXML
    private ColorPicker colorPicker;



    @FXML
    public void initialize() {
        createTableBtn.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                screenService = ScreenService.getInstance((Stage) createTableBtn.getScene().getWindow());
            }
        });
        backToMenuBtn.setText(EString.BACK_TO_MENU.toString());
        joinTableBtn.setText(EString.JOIN_TAB.toString());
        joinTableLabel.setText(EString.LABEL_JOIN_TAB.toString());
        createTableBtn.setText(EString.CREATE_TAB.toString());
        nameTextField.setPromptText(EString.NAME_TAB.toString());
        descriptionTextField.setPromptText(EString.DESCRIPTION_TAB.toString());
        labelTextField.setPromptText("Frond-end");
        joinCodeTextField.setPromptText("Code du tableau");
        colorLabel.setText(EString.SELECT_COLOR.toString());
        labelEtiquette.setText(EString.LABEL.toString());
        descriptionLabel.setText(EString.DESCRIPTION.toString());
        nameLabel.setText(EString.NAME.toString());
        addLabelBtn.setText(EString.ADD_LABEL.toString());

        backToMenuBtn.setOnAction(event -> screenService.setScreen(EPage.INDEX));
        createTableBtn.setOnAction(event -> {
            ProjectApiClient projectApiClient = ProjectApiClient.getInstance();
            if (nameTextField.getText().isEmpty()) {
                setErrorMessage(nameTextField);
                return;
            }

            projectApiClient.createProject(new Project(nameTextField.getText(), descriptionTextField.getText()));
            screenService.loadScreen(EPage.SHOW_TAB, ShowTabController::new);
            screenService.setScreen(EPage.SHOW_TAB);
            resetFields();
        });

        addLabelBtn.setOnAction(event -> {
            LabelApiClient labelApiClient = LabelApiClient.getInstance();
            if (labelTextField.getText().isEmpty()) {
                setErrorMessage(labelTextField);
                return;
            }

            labelApiClient.addLabel(new LabelModel(labelTextField.getText(), colorPicker.getValue()));
            System.out.println(labelApiClient.getLabels());
        });
    }

    private void setErrorMessage(TextField textField) {
        textField.setStyle("-fx-prompt-text-fill: red;");
        textField.setPromptText(EString.EMPTY_FIELD.toString());
    }

    private void resetErrorMessage(TextField textField) {
        textField.setStyle("-fx-prompt-text-fill: black;");
        textField.setPromptText("");
    }

    // reset all fields
    private void resetFields() {
        nameTextField.setText("");
        descriptionTextField.setText("");
        labelTextField.setText("");
        joinCodeTextField.setText("");
    }

}