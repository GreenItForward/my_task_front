package com.mytask.front.controller;

import com.mytask.front.model.EPage;
import com.mytask.front.service.ScreenService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreateTabController {
    private final ScreenService screenService;

    @FXML
    private Button backToMenuBtn;
    @FXML
    private Button joinTableBtn;

    @FXML
    private Label joinTableLabel;

    @FXML
    private Button createTableBtn;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField joinCodeTextField;

    @FXML
    private TextField labelTextField;

    @FXML
    private Label colorLabel;

    @FXML
    private Label labelEtiquette;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label nameLabel;


    public CreateTabController(ScreenService screenService) {
        this.screenService = screenService;
    }

    @FXML
    public void initialize() {
        backToMenuBtn.setText("Retour au menu");
        joinTableBtn.setText("Rejoindre le tableau");
        joinTableLabel.setText("Entrez le code du tableau que vous souhaitez rejoindre :");
        createTableBtn.setText("Créer le tableau");
        nameTextField.setPromptText("Nom du tableau");
        descriptionTextField.setPromptText("La description du tableau");
        labelTextField.setPromptText("Frond-end");
        joinCodeTextField.setPromptText("Code du tableau");
        colorLabel.setText("Sélection de couleur");
        labelEtiquette.setText("Etiquette");
        descriptionLabel.setText("Description");
        nameLabel.setText("Nom");
        backToMenuBtn.setOnAction(event -> screenService.setScreen(EPage.INDEX));
    }


}