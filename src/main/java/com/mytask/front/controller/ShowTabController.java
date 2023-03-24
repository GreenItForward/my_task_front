package com.mytask.front.controller;

import com.mytask.front.model.EPage;
import com.mytask.front.service.ScreenService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ShowTabController {

    @FXML
    private Label myTablesLabel, tableInfoLabel, descriptionLabel,
            todoLabel, inProgressLabel, doneLabel;

    @FXML
    private Button backToMenuBtn, addTaskBtn, editTableBtn, deleteTableBtn;

    private ScreenService screenService;

    public ShowTabController(ScreenService screenService) {
        this.screenService = screenService;
    }

    public void initialize() {
        myTablesLabel.setText("Mes tableaux");
        tableInfoLabel.setText("Informations du tableau");
        descriptionLabel.setText("Description");
        todoLabel.setText("TODO");
        inProgressLabel.setText("IN PROGRESS");
        doneLabel.setText("DONE");
        backToMenuBtn.setText("Retour au menu");
        addTaskBtn.setText("Ajouter une tâche");
        editTableBtn.setText("Éditer le tableau");
        deleteTableBtn.setText("Supprimer le tableau");
        backToMenuBtn.setOnAction(event -> screenService.setScreen(EPage.INDEX));
    }

}