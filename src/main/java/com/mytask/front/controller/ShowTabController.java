package com.mytask.front.controller;

import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Project;
import com.mytask.front.model.Task;
import com.mytask.front.service.api.impl.TaskApiClient;
import com.mytask.front.service.api.impl.TaskLabelApiClient;
import com.mytask.front.service.view.PopupService;
import com.mytask.front.utils.EPage;
import com.mytask.front.service.view.ScreenService;
import com.mytask.front.service.view.TabService;
import com.mytask.front.utils.EStatus;
import com.mytask.front.utils.EString;
import com.mytask.front.utils.PdfExportHelper;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;
import javafx.scene.control.TextField;

import static com.mytask.front.configuration.AppConfiguration.labels;
import static com.mytask.front.service.view.PopupService.showTablesPopup;
import static com.mytask.front.utils.EStatus.IN_PROGRESS;

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
    private static ShowTabController instance;
    private Project project;

    static {
        try { rand = SecureRandom.getInstanceStrong(); }
        catch (NoSuchAlgorithmException e) { rand = new SecureRandom(); }
    }

    private ShowTabController() {
    }


    public static ShowTabController getInstance() {
        if (instance == null) {
            instance = new ShowTabController();
        }
        return instance;
    }

    @FXML
    public void initialize() {
        initData();
        setTextForUIElements();
        configureButtons();
        setupDragAndDrop();
        initializeTaskLists();
    }

    private void initData() {
      //  project.setLabels(labels);
    }

    private void setTextForUIElements() {
        tableLabel.setText(EString.MY_TABS.toString());
        backToMenuBtn.setText(EString.BACK_TO_MENU.toString());
        generateInviteCodeBtn.setText(EString.GENERATE_INVITE_CODE.toString());
        viewMembersBtn.setText(EString.VIEW_MEMBERS.toString());
        todoLabel.setText(EString.TODO.toString());
        inProgressLabel.setText(EString.IN_PROGRESS.toString());
        doneLabel.setText(EString.DONE.toString());
        showTablesBtn.setText(EString.SHOW_TABLES.toString());
        exportToPdfBtn.setText(EString.EXPORT_TO_PDF.toString());
    }

    private void configureButtons() {
        backToMenuBtn.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                screenService = ScreenService.getInstance((Stage) backToMenuBtn.getScene().getWindow());
            }
        });

        //TODO: Initialize the controller's logic here
        // ex: add EventHandlers to buttons, set initial data, etc.
        backToMenuBtn.setOnAction(event -> screenService.setScreen(EPage.INDEX));

        // quand on appuie sur viewMemberBTn on affiche un popup avec la liste des membres de la table
        viewMembersBtn.setOnAction(event -> TabService.showMembers((Stage) viewMembersBtn.getScene().getWindow()));

        // quand on appuie sur generateInviteCodeBtn on affiche un popup avec le code d'invitation
        generateInviteCodeBtn.setOnAction(event -> TabService.showInviteCode((Stage) generateInviteCodeBtn.getScene().getWindow()));

        // quand on appuie sur showTablesBtn on affiche un popup avec la liste des tableaux
        showTablesBtn.setOnAction(event -> showTablesPopup());

        // quand on appuie sur exportToPdfBtn on exporte la table en pdf
        exportToPdfBtn.setOnAction(event -> PdfExportHelper.exportToPdf(getTasksByColumn()));
    }

    private List<VBox> getTasksByColumn() {
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

        return tasksByColumn;
    }

    private void initializeTaskLists() {
        todoTasksList.getChildren().add(0, createAddTaskField(todoTasksList));
        inProgressTasksList.getChildren().add(0,  createAddTaskField(inProgressTasksList));
        doneTasksList.getChildren().add(0, createAddTaskField(doneTasksList));
        todoTasksList.setId(EStatus.TODO.getValue());
        inProgressTasksList.setId(IN_PROGRESS.getValue());
        doneTasksList.setId(EStatus.DONE.getValue());
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
                Task task = new Task();
                HBox newTask = createRandomTask(task, taskText);
                taskList.getChildren().add(taskList.getChildren().size(), newTask);
                taskList.setUserData(task);
                task.setStatus(taskList.getId());
                TaskApiClient.getInstance().createTask(task);
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
    private HBox createRandomTask(Task task, String title) {
        HBox taskBox = new HBox(10);
        Random random = new Random();

        List<LabelModel> labels = this.project.getLabels();
        List<LabelModel> taskLabels = new ArrayList<>();
        taskLabels.add(labels.get(0));
        taskLabels.add(labels.get(1));
        HBox colorTags = TabService.createColorTags(task);
        task.setLabels(taskLabels);
        task.setTitle(title);
        task.setProjectID(this.project.getId());

        //TODO: quand on créera les taches sans les données aléatoires, on devra enlever ça et mettre les données de la tache (projectID, assignedTo, etc.)
        Label titleLabel = new Label(title);
        titleLabel.textProperty().bind(task.titleProperty());
        HBox deadlineBox = TabService.createDeadlineBox(task);

        TextField assignedToField = TabService.createAssignedToField(random);
        assignedToField.textProperty().bindBidirectional(task.assignedToProperty());
        VBox titleAndTags = new VBox(colorTags, titleLabel, deadlineBox, assignedToField);

        ImageView editImageView = TabService.createEditImageView();
        editImageView.setOnMouseClicked(e -> {
            System.out.println("Edit task");
            PopupService.showTaskDetailPopup((Stage) editImageView.getScene().getWindow(), task);
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

        task.setLabelBox(colorTags);
        task.setTaskBox(taskBox);

        taskBox.setUserData(task);
        return taskBox;
    }

    private VBox createRandomTasksRecursively(Random random, int nbTasks) {
        if (nbTasks <= 0) {
            return new VBox();
        }

        HBox taskBox = createRandomTask(new Task(), "Task " + nbTasks);
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
                if (event.getGestureSource() != column && event.getDragboard().hasString() && "task".equals(event.getDragboard().getString())) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            scrollPane.setOnDragDropped(event -> {
                if (draggedTask != null && !Objects.equals(draggedTask.getParent(), column)) {
                    column.getChildren().add(draggedTask);
                    event.setDropCompleted(true);

                    String targetParentId = column.getId();
                    Task task = (Task) draggedTask.getUserData();

                    switch (targetParentId) {
                        case "TODO" -> task.setStatus(EStatus.TODO.getValue());
                        case "IN PROGRESS" -> task.setStatus(IN_PROGRESS.getValue());
                        case "DONE" -> task.setStatus(EStatus.DONE.getValue());
                    }


                    TaskApiClient.getInstance().updateTask(task);

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

    public void updateLabels(Task Task) {
        HBox taskBox = Task.getLabelBox();

        taskBox.getChildren().removeIf(Rectangle.class::isInstance);
        taskBox.getChildren().removeIf(HBox.class::isInstance);

        HBox colorTags = TabService.createColorTags(Task);
        taskBox.getChildren().add(colorTags);

        TaskLabelApiClient taskLabelApi = TaskLabelApiClient.getInstance();
       // Task.getLabels().forEach(label -> taskLabelApi.updateLabelToTask(Task, label)); // à utiliser quand on aura l'API pour mettre à jour les labels d'une tache
        taskLabelApi.updateLabelToTask(Task, Task.getLabels().get(0));
    }

    public VBox getTodoTasksList() {
        return todoTasksList;
    }

    public VBox getInProgressTasksList() {
        return inProgressTasksList;
    }

    public VBox getDoneTasksList() {
        return doneTasksList;
    }

    public void setProject(Project project) {
        this.project = project;
        project.setLabels(labels);
        project.getTasks().forEach(task -> {
            createRandomTask(task, task.getTitle());
            switch (task.getStatus()) {
                case "TODO" -> todoTasksList.getChildren().add(task.getTaskBox());
                case "IN PROGRESS" -> inProgressTasksList.getChildren().add(task.getTaskBox());
                case "DONE" -> doneTasksList.getChildren().add(task.getTaskBox());
            }
        });

    }

    public Project getProject() {
        return project;
    }
}
