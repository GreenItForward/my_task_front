package com.mytask.front.controller;

import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Project;
import com.mytask.front.service.api.impl.LabelApiClient;
import com.mytask.front.service.api.impl.ProjectApiClient;
import com.mytask.front.service.view.TabService;
import com.mytask.front.utils.enums.EPage;
import com.mytask.front.service.view.ScreenService;
import com.mytask.front.utils.enums.EString;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import org.json.JSONException;

import java.util.ArrayList;

import static javafx.scene.Cursor.DEFAULT;
import static javafx.scene.Cursor.HAND;

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
    private HBox labelHBox;

    LabelApiClient labelApiClient;

    @FXML
    public void initialize() {
        initData();
        configureButtons();
        setTextForUIElements();
    }

    private void initData() {
        this.labelApiClient = LabelApiClient.getInstance();
    }

    private void configureButtons() {
        createTableBtn.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                screenService = ScreenService.getInstance((Stage) createTableBtn.getScene().getWindow());
            }
        });

        backToMenuBtn.setOnAction(event -> screenService.setScreen(EPage.INDEX));
        createTableBtn.setOnAction(event -> {
            try {
                createTable();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        addLabelBtn.setOnAction(event -> addLabel());
    }

    private void setTextForUIElements() {
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
    }

    private void createTable() throws JSONException {
        ProjectApiClient projectApiClient = ProjectApiClient.getInstance();
        if (nameTextField.getText().isEmpty()) {
            setErrorMessage(nameTextField);
            return;
        }

        Project project = new Project(nameTextField.getText(), descriptionTextField.getText());
        project.setLabels(labelApiClient.getLabels(project));
        Project newProject = projectApiClient.createProject(project);
        project.setId(newProject.getId());
        ShowTabController.getInstance().setProject(project);
        ShowTabController.getInstance().resetController();
        ShowTabController.getInstance().refreshTasks();
        Project.setTasks(new ArrayList<>());
        screenService.loadScreen(EPage.SHOW_TAB, ShowTabController::getInstance);
        screenService.setScreen(EPage.SHOW_TAB);
        resetFields(null);
    }

    private void addLabel() {
        if (labelTextField.getText().isEmpty()) {
            setErrorMessage(labelTextField);
            return;
        }

        LabelModel labelModel = new LabelModel(labelTextField.getText(), colorPicker.getValue());
        labelApiClient.addLabel(labelModel);
        createRectangle(labelModel);
        resetFields(labelTextField);
    }

    private void setErrorMessage(TextField textField) {
        textField.setStyle("-fx-border-color: red");
        textField.setPromptText(EString.ERROR.toString());
    }

    private void resetErrorMessage(TextField textField) {
        textField.setStyle("-fx-prompt-text-fill: black;");
        textField.setPromptText("");
    }

    // reset all fields
    private void resetFields(TextField textField) {
        if (textField == null) {
            nameTextField.setText("");
            descriptionTextField.setText("");
            labelTextField.setText("");
            joinCodeTextField.setText("");
        } else {
            textField.setText("");
        }

        resetErrorMessage(nameTextField);
    }

    private void createRectangle(LabelModel labelModel) {
        Rectangle rectangle = new Rectangle(20, 10, colorPicker.getValue());
        rectangle.setStroke(Color.BLACK);
        Label label = new Label(labelTextField.getText());
        label.setTextFill(Color.BLACK);

        HBox labelContainer = new HBox(rectangle, label);
        labelContainer.setSpacing(5);

        labelHBox.getChildren().add(labelContainer);
        rectangle.setOnMouseClicked(e -> {
            labelHBox.getChildren().remove(labelContainer);
            this.labelApiClient.removeLabel(labelModel);
        });

        rectangle.setOnMouseEntered(e -> rectangle.setCursor(HAND));
        rectangle.setOnMouseExited(e -> rectangle.setCursor(DEFAULT));
    }

}