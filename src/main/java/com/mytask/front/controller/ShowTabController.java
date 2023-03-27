package com.mytask.front.controller;

import com.mytask.front.model.EPage;
import com.mytask.front.service.ScreenService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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

        // Ajouter des événements de glisser-déposer

        setupDragAndDrop();

        //TODO: Initialize the controller's logic here
        // ex: add EventHandlers to buttons, set initial data, etc.
        backToMenuBtn.setOnAction(event -> screenService.setScreen(EPage.INDEX));

    }


    // Ajouter des tâches aléatoires (pour les tests avant d'implémenter l'API)
    private HBox createRandomTask(Random random) {
        HBox taskBox = new HBox(10);

        String labelName1 = "Étiquette " + (random.nextInt(100) + 1);
        String labelName2 = "Étiquette " + (random.nextInt(100) + 1);
        Rectangle colorRect1 = new Rectangle(20, 10, Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        Rectangle colorRect2 = new Rectangle(20, 10, Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));

        colorRect1.setArcWidth(10);
        colorRect1.setArcHeight(10);
        colorRect2.setArcWidth(10);
        colorRect2.setArcHeight(10);

        Tooltip tooltip1 = new Tooltip(labelName1);
        Tooltip tooltip2 = new Tooltip(labelName2);
        Tooltip.install(colorRect1, tooltip1);
        Tooltip.install(colorRect2, tooltip2);

        HBox colorTags = new HBox(5, colorRect1, colorRect2);

        Label titleLabel = new Label("[JAVA]  Mise en place de JavaFX " + (random.nextInt(100) + 1));

        Image clockImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/mytask/front/icons/horloge.png")));
        Image clockImageChecked  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/mytask/front/icons/check.png")));

        ImageView clockImageView = new ImageView(clockImage);
        clockImageView.setFitHeight(15);
        clockImageView.setFitWidth(15);
        Label dueDateLabel = new Label((random.nextInt(30) + 1) + " avr");
        dueDateLabel.getStyleClass().add("dueDateLabel");

        Button dueDateButton = new Button();
        dueDateButton.setGraphic(clockImageView);
        dueDateButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

        dueDateButton.setOnAction(e -> {
            if (clockImageView.getImage() == clockImage) {
                clockImageView.setImage(clockImageChecked);
                dueDateLabel.getStyleClass().add("dueDateLabelChecked");
            } else {
                clockImageView.setImage(clockImage);
                dueDateLabel.getStyleClass().remove("dueDateLabelChecked");
            }
        });

        HBox deadlineBox = new HBox(5, dueDateButton, dueDateLabel);

        TextField assignedToField = new TextField("Personne " + (random.nextInt(10) + 1));
        assignedToField.setEditable(false);
        assignedToField.getStyleClass().add("dueDateLabel");

        VBox titleAndTags = new VBox(colorTags, titleLabel, deadlineBox, assignedToField);


        taskBox.getChildren().addAll(titleAndTags);
        configureTaskDragAndDrop(taskBox);

        // Ajouter un margin top pour espacer les tâches
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
