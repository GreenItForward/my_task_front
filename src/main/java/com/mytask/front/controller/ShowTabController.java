package com.mytask.front.controller;

import com.mytask.front.service.view.PopupService;
import com.mytask.front.utils.EPage;
import com.mytask.front.service.view.ScreenService;
import com.mytask.front.service.view.TabService;
import com.mytask.front.utils.EString;
import com.mytask.front.utils.PdfExportService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import javafx.scene.control.TextField;
import static com.mytask.front.service.view.PopupService.showTablesPopup;

public class ShowTabController {

    ScreenService screenService;

    @FXML
    private Label tableLabel;
    @FXML
    private Label todoLabel;
    @FXML
    private Label inProgressLabel;
    @FXML
    private Label doneLabel;
    @FXML
    private Button backToMenuBtn;
    @FXML
    private Button generateInviteCodeBtn;
    @FXML
    private Button viewMembersBtn;
    @FXML
    private Button exportToPdfBtn;
    @FXML
    private VBox todoTasksList;
    @FXML
    private VBox inProgressTasksList;
    @FXML
    private VBox doneTasksList;

    @FXML
    private ScrollPane todoTask;
    @FXML
    private ScrollPane inProgressTask;
    @FXML
    private ScrollPane doneTask;

    @FXML
    private Button showTablesBtn;

    private HBox draggedTask;
    private static SecureRandom rand;

    static {
        try { rand = SecureRandom.getInstanceStrong(); }
        catch (NoSuchAlgorithmException e) { rand = new SecureRandom(); }
    }

    @FXML
    public void initialize() {
        backToMenuBtn.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                screenService = ScreenService.getInstance((Stage) backToMenuBtn.getScene().getWindow());
            }
        });
        tableLabel.setText(EString.MY_TABS.toString());
        backToMenuBtn.setText(EString.BACK_TO_MENU.toString());
        generateInviteCodeBtn.setText(EString.GENERATE_INVITE_CODE.toString());
        viewMembersBtn.setText(EString.VIEW_MEMBERS.toString());
        todoLabel.setText(EString.TODO.toString());
        inProgressLabel.setText(EString.IN_PROGRESS.toString());
        doneLabel.setText(EString.DONE.toString());
        showTablesBtn.setText(EString.SHOW_TABLES.toString());
        exportToPdfBtn.setText(EString.EXPORT_TO_PDF.toString());
        TextField addTodoTaskField = createAddTaskField(todoTasksList);
        TextField addInProgressTaskField = createAddTaskField(inProgressTasksList);
        TextField addDoneTaskField = createAddTaskField(doneTasksList);

        // Ajouter des tâches aléatoires (pour les tests avant d'implémenter l'API)
        /* // MOCK DATA TO TEST THE UI
        todoTasksList.getChildren().add(createRandomTasksRecursively(rand, 5));
        inProgressTasksList.getChildren().add(createRandomTasksRecursively(rand, 5));
        doneTasksList.getChildren().add(createRandomTasksRecursively(rand, 5));
        */

        todoTasksList.getChildren().add(0, addTodoTaskField);
        inProgressTasksList.getChildren().add(0, addInProgressTaskField);
        doneTasksList.getChildren().add(0, addDoneTaskField);
        setupDragAndDrop();

        //TODO: Initialize the controller's logic here
        // ex: add EventHandlers to buttons, set initial data, etc.
        backToMenuBtn.setOnAction(event -> screenService.setScreen(EPage.INDEX));

        // quand on appuie sur viewMemberBTn on affiche un popup avec la liste des membres de la table
        viewMembersBtn.setOnAction(event -> TabService.showMembers((Stage) viewMembersBtn.getScene().getWindow()));

        // quand on appuie sur generateInviteCodeBtn on affiche un popup avec le code d'invitation
        generateInviteCodeBtn.setOnAction(event -> TabService.showInviteCode((Stage) generateInviteCodeBtn.getScene().getWindow()));

        List<VBox> tasksByColumn = new ArrayList<>();

        // vérifier que si il sont vide alors on les ajoute pas
        if (todoTasksList.getChildren().size() > 1) {
            tasksByColumn.add((VBox) todoTasksList.getChildren().get(1));
        }

        if (inProgressTasksList.getChildren().size() > 1) {
            tasksByColumn.add((VBox) inProgressTasksList.getChildren().get(1));
        }

        if (doneTasksList.getChildren().size() > 1) {
            tasksByColumn.add((VBox) doneTasksList.getChildren().get(1));
        }
        
        // quand on appuie sur showTablesBtn on affiche un popup avec la liste des tableaux
        showTablesBtn.setOnAction(event -> showTablesPopup());
        
        // quand on appuie sur exportToPdfBtn on exporte la table en pdf
       exportToPdfBtn.setOnAction(event -> PdfExportService.exportToPdf(tasksByColumn));
    }


    private TextField createAddTaskField(VBox taskList) {
        TextField addTaskField = new TextField(EString.ADD_TASK.toString());
        addTaskField.getStyleClass().add("add-task-field");

        // Rendre le champ non modifiable jusqu'à ce que l'utilisateur clique dessus
        addTaskField.setEditable(false);
        addTaskField.setOnMouseClicked(event -> {
            if (!addTaskField.isEditable()) {
                addTaskField.setEditable(true);
                addTaskField.setText(""); // Vider le texte lorsqu'il est cliqué
            }
        });

        // lorsque l'utilisateur appuie sur Entrée après avoir modifié le texte, on ajoute une nouvelle tâche
        addTaskField.setOnAction(event -> {
            String taskText = addTaskField.getText();
            if (!taskText.isBlank() && !taskText.equals(EString.ADD_TASK.toString())) {
                HBox newTask = createRandomTask(rand, taskText);
                taskList.getChildren().add(taskList.getChildren().size(), newTask);

                // on remet le champ à son état initial
                addTaskField.setEditable(false);
                addTaskField.setText(EString.ADD_TASK.toString());
            }
        });

        // quand l'utilisateur clique en dehors du champ, on remet le champ à son état initial
        addTaskField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (Boolean.TRUE.equals(!newValue) && addTaskField.getText().isEmpty()) {
                addTaskField.setText(EString.ADD_TASK.toString());
                addTaskField.setEditable(false);
            }
        });

        return addTaskField;
    }


    // Ajouter des tâches aléatoires (pour les tests avant d'implémenter l'API)
    private HBox createRandomTask(Random random, String title) {
        HBox taskBox = new HBox(10);

        HBox colorTags = TabService.createColorTags(random);
        Label titleLabel = title != null ? new Label(title) : TabService.createTitleLabel(random);
        HBox deadlineBox = TabService.createDeadlineBox(random);
        TextField assignedToField = TabService.createAssignedToField(random);

        VBox titleAndTags = new VBox(colorTags, titleLabel, deadlineBox, assignedToField);

        ImageView editImageView = TabService.createEditImageView();
        editImageView.setOnMouseClicked(e -> {
            System.out.println("Edit task");
            PopupService.showTaskDetailPopup((Stage) editImageView.getScene().getWindow());
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

    private VBox createRandomTasksRecursively(Random random, int nbTasks) {
        if (nbTasks <= 0) {
            return new VBox();
        }

        HBox taskBox = createRandomTask(random, null);
        VBox tasksContainer = createRandomTasksRecursively(random, nbTasks - 1);
        tasksContainer.getChildren().add(taskBox);
        VBox.setMargin(taskBox, new Insets(10, 0, 0, 0));

        return tasksContainer;
    }

    private void setupDragAndDrop() {
        ScrollPane[] scrollPanes = new ScrollPane[]{todoTask, inProgressTask, doneTask};

        for (ScrollPane scrollPane : scrollPanes) {
            VBox column = (VBox) scrollPane.getContent();

            scrollPane.setOnDragOver(event -> {
                if (event.getGestureSource() != column && event.getDragboard().hasString() && "task".equals(event.getDragboard().toString())) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            scrollPane.setOnDragDropped(event -> {
                if (draggedTask != null && !Objects.equals(draggedTask.getParent(), column)) {
                    column.getChildren().add(draggedTask);
                    event.setDropCompleted(true);
                    // TODO: update task status in list
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
