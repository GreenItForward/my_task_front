package com.mytask.front.controller;

import com.mytask.front.utils.EPage;
import com.mytask.front.service.ScreenService;
import com.mytask.front.service.TabService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import java.util.Objects;
import java.util.Random;

public class ShowTabController {

    ScreenService screenService;

    @FXML
    private Label tablesLabel, todoLabel, inProgressLabel, doneLabel;
    @FXML
    private ListView<?> tablesListView;
    @FXML
    private Button backToMenuBtn, generateInviteCodeBtn, viewMembersBtn;
    
    @FXML
    private VBox todoTasksList, inProgressTasksList, doneTasksList;

    @FXML
    private ScrollPane todoTask, inProgressTask, doneTask;

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

        // Ajouter des tâches aléatoires (pour les tests avant d'implémenter l'API)
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            todoTasksList.getChildren().add(createRandomTask(random));
            inProgressTasksList.getChildren().add(createRandomTask(random));
            doneTasksList.getChildren().add(createRandomTask(random));
        }

        setupDragAndDrop();

        //TODO: Initialize the controller's logic here
        // ex: add EventHandlers to buttons, set initial data, etc.
        backToMenuBtn.setOnAction(event -> screenService.setScreen(EPage.INDEX));

    }


    // Ajouter des tâches aléatoires (pour les tests avant d'implémenter l'API)
    private HBox createRandomTask(Random random) {
        HBox taskBox = new HBox(10);

        HBox colorTags = TabService.createColorTags(random);
        Label titleLabel = TabService.createTitleLabel(random);
        HBox deadlineBox = TabService.createDeadlineBox(random);
        TextField assignedToField = TabService.createAssignedToField(random);

        VBox titleAndTags = new VBox(colorTags, titleLabel, deadlineBox, assignedToField);

        ImageView editImageView = TabService.createEditImageView();
        editImageView.setOnMouseClicked(e -> {
            System.out.println("Edit");
        });
        taskBox.setOnMouseEntered(e -> {
                editImageView.setVisible(true);
                taskBox.setCursor(Cursor.HAND);
        });

        taskBox.setOnMouseExited(e -> {
                editImageView.setVisible(false);
                taskBox.setCursor(Cursor.DEFAULT);
        });

        HBox taskContentAndEditLogo = new HBox();
        taskContentAndEditLogo.getChildren().addAll(titleAndTags, editImageView);

        HBox.setHgrow(titleAndTags, Priority.ALWAYS);

        taskBox.getChildren().addAll(taskContentAndEditLogo);
        configureTaskDragAndDrop(taskBox);

        VBox.setMargin(taskBox, new Insets(10, 0, 0, 0));

        return taskBox;
    }


    private void setupDragAndDrop() {
        ScrollPane[] scrollPanes = new ScrollPane[]{todoTask, inProgressTask, doneTask};

        for (ScrollPane scrollPane : scrollPanes) {
            VBox column = (VBox) scrollPane.getContent();

            scrollPane.setOnDragOver(event -> {
                if (event.getGestureSource() != column && event.getDragboard().hasString() && "task".equals(event.getDragboard().getString())) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            scrollPane.setOnDragDropped(event -> {
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


    private void configureTaskDragAndDrop(HBox taskBox) {
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
                VBox targetParent = (VBox) draggedTask.getParent();
                targetParent.getChildren().remove(draggedTask);
                targetParent.getChildren().add(draggedTask);
            }

            draggedTask = null;
            event.consume();
        });
    }


}
