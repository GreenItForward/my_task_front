package com.mytask.front.controller;

import com.mytask.front.model.EPage;
import com.mytask.front.service.ScreenService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Objects;
import java.util.Random;

public class ShowTabController {

    ScreenService screenService;

    @FXML
    private Label tablesLabel, tableTitleLabel, tableShareCodeLabel, tableDescriptionLabel, todoLabel, inProgressLabel, doneLabel;
    @FXML
    private ListView<?> tablesListView;
    @FXML
    private Button backToMenuBtn, generateInviteCodeBtn, viewMembersBtn;
    
    @FXML
    private VBox tableInfoVBox, todoColumn, inProgressColumn, doneColumn,  todoTasksList, inProgressTasksList, doneTasksList;

    private HBox draggedTask;


    public ShowTabController(ScreenService screenService) {
        this.screenService = screenService;
    }

    public void initialize() {
        tablesLabel.setText("Mes tableaux");
        backToMenuBtn.setText("Retour au menu");
        generateInviteCodeBtn.setText("Générer un code d'invitation");
        viewMembersBtn.setText("Voir les membres");
        todoLabel.setText("TODO");
        inProgressLabel.setText("IN PROGRESS");
        doneLabel.setText("DONE");

        // Ajouter des tâches aléatoires
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            todoTasksList.getChildren().add(createRandomTask(random));
            inProgressTasksList.getChildren().add(createRandomTask(random));
            doneTasksList.getChildren().add(createRandomTask(random));
        }

        // Ajouter des événements de glisser-déposer
        setupDragAndDrop();

        //TODO: Initialize the controller's logic here
        // ex: add EventHandlers to buttons, set initial data, etc.
        backToMenuBtn.setOnAction(event -> screenService.setScreen(EPage.INDEX));

    }


    private HBox createRandomTask(Random random) {
        HBox taskBox = new HBox(10);
        Label titleLabel = new Label("Tâche " + (random.nextInt(100) + 1));
        Rectangle colorRect = new Rectangle(10, 10, Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        TextField assignedToField = new TextField("Personne " + (random.nextInt(10) + 1));
        assignedToField.setEditable(false);
        Label dueDateLabel = new Label("Échéance : " + (random.nextInt(30) + 1) + "/" + (random.nextInt(12) + 1) + "/2023");

        taskBox.getChildren().addAll(titleLabel, colorRect, assignedToField, dueDateLabel);
        return taskBox;
    }

    private void setupDragAndDrop() {
        VBox todoVBox = todoTasksList;
        VBox inProgressVBox = inProgressTasksList;
        VBox doneVBox = doneTasksList;

        for (VBox column : new VBox[]{todoVBox, inProgressVBox, doneVBox}) {
            for (Node taskNode : column.getChildren()) {
                if (taskNode instanceof HBox) {
                    HBox taskBox = (HBox) taskNode;
                    taskBox.setOnDragDetected(event -> {
                        Dragboard db = taskBox.startDragAndDrop(TransferMode.MOVE);
                        ClipboardContent content = new ClipboardContent();
                        content.putString("task");
                        db.setContent(content);
                        draggedTask = taskBox;
                        event.consume();
                    });

                    taskBox.setOnDragDone(event -> {
                        if (event.getTransferMode() == TransferMode.MOVE) {
                            ((VBox) draggedTask.getParent()).getChildren().remove(draggedTask);
                        }
                        draggedTask = null;
                        event.consume();
                    });
                }
            }

            column.setOnDragOver(event -> {
                if (event.getDragboard().hasString() && "task".equals(event.getDragboard().getString())) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            column.setOnDragDropped(event -> {
                if (draggedTask != null && !Objects.equals(draggedTask.getParent(), column)) {
                    column.getChildren().add(draggedTask);
                    event.setDropCompleted(true);
                } else {
                    event.setDropCompleted(false);
                }
                event.consume();
            });
        }
    }


}
