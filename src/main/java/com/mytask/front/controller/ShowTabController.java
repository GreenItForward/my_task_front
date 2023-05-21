package com.mytask.front.controller;

import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Project;
import com.mytask.front.model.Task;
import com.mytask.front.model.User;
import com.mytask.front.service.api.impl.LabelApiClient;
import com.mytask.front.service.api.impl.RoleApiClient;
import com.mytask.front.service.api.impl.TaskApiClient;
import com.mytask.front.service.api.impl.TaskLabelApiClient;
import com.mytask.front.service.view.PopupService;
import com.mytask.front.service.view.ScreenService;
import com.mytask.front.service.view.TabService;
import com.mytask.front.utils.enums.EPage;
import com.mytask.front.utils.enums.EStatus;
import com.mytask.front.utils.enums.EString;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.json.JSONException;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.mytask.front.service.view.PopupService.showTablesPopup;
import static com.mytask.front.service.view.TabService.createAddTaskField;
import static com.mytask.front.utils.enums.EStatus.IN_PROGRESS;

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
    private Button exportToBtn;
    @FXML private Button projectSettingBtn;
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
    private Task currentTask;

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
    public void initialize() throws JSONException {
        initData();
        setTextForUIElements();
        configureButtons();
        setupDragAndDrop();
        initializeTaskLists();
    }

    private void initData() {
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
        exportToBtn.setText(EString.EXPORT.toString());
        projectSettingBtn.setText(EString.PROJECT_SETTINGS.toString());
    }

    private void configureButtons() {
        backToMenuBtn.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                screenService = ScreenService.getInstance((Stage) backToMenuBtn.getScene().getWindow());
            }
        });

        projectSettingBtn.setOnAction(event -> PopupService.showProjectSettingsPopup((Stage) projectSettingBtn.getScene().getWindow(), project));
        backToMenuBtn.setOnAction(event -> {
            screenService.setScreen(EPage.SHOW_ALL_TAB);
            try {
                resetController();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });


        // quand on appuie sur viewMemberBTn on affiche un popup avec la liste des membres de la table
        viewMembersBtn.setOnAction(event -> TabService.showMembers((Stage) viewMembersBtn.getScene().getWindow()));

        // quand on appuie sur generateInviteCodeBtn on affiche un popup avec le code d'invitation
        generateInviteCodeBtn.setOnAction(event -> TabService.showInviteCode((Stage) generateInviteCodeBtn.getScene().getWindow()));

        // quand on appuie sur showTablesBtn on affiche un popup avec la liste des tableaux
        showTablesBtn.setOnAction(event -> showTablesPopup());

        // quand on appuie sur exportToBtn on ouvre un Popup pour exporter le tableau en pdf ou en csv
        exportToBtn.setOnAction(event -> PopupService.showExportPopup((Stage) exportToBtn.getScene().getWindow(), project));
    }

    private List<VBox> getTasksByColumn() {
        List<VBox> tasksByColumn = new ArrayList<>();

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

    public void initializeTaskLists() {
        todoTasksList.getChildren().add(0, createAddTaskField(todoTasksList));
        inProgressTasksList.getChildren().add(0, createAddTaskField(inProgressTasksList));
        doneTasksList.getChildren().add(0, createAddTaskField(doneTasksList));

        todoTasksList.setId(EStatus.TODO.getValue());
        inProgressTasksList.setId(IN_PROGRESS.getValue());
        doneTasksList.setId(EStatus.DONE.getValue());
    }

    // Ajouter une tache dans la liste des taches
    public HBox createTask(Task task, String title) {
        HBox taskBox = new HBox(10);
        task.setTitle(title);
        task.setProjectID(this.project.getId());
        try {
            task.setLabels(TaskLabelApiClient.getInstance().getLabelsByTaskId(task.getId(), project.getId()));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        HBox colorTags = TabService.createColorTags(task);
        task.setTaskBox(colorTags);

        Label titleLabel = new Label(title);
        titleLabel.textProperty().bind(task.titleProperty());
        HBox deadlineBox = TabService.createDeadlineBox(task);

        TextField assignedToField = TabService.createAssignedToField();



        assignedToField.textProperty().bindBidirectional(new SimpleStringProperty(task.getAssignedTo().getPrenom()));
        VBox titleAndTags = new VBox(colorTags, titleLabel, deadlineBox, assignedToField);

        ImageView editImageView = TabService.createEditImageView();
        editImageView.setOnMouseClicked(e -> PopupService.showTaskDetailPopup((Stage) editImageView.getScene().getWindow(), task));

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

        HBox taskBox = createTask(new Task(), "Task " + nbTasks);
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
                    task.setStatus(targetParentId);
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

    public void updateLabels(Task Task, LabelModel label) {
        HBox taskBox = Task.getLabelBox();

        taskBox.getChildren().removeIf(Rectangle.class::isInstance);
        taskBox.getChildren().removeIf(HBox.class::isInstance);

        HBox colorTags = TabService.createColorTags(Task);
        taskBox.getChildren().add(colorTags);

       // Task.getLabels().forEach(label -> taskLabelApi.updateLabelToTask(Task, label)); // à utiliser quand on aura l'API pour mettre à jour les labels d'une tache
        TaskLabelApiClient taskLabelApi = TaskLabelApiClient.getInstance();
        taskLabelApi.updateLabelToTask(Task, label);

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

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public void setProject(Project project, VBox todoTasksList, VBox inProgressTasksList, VBox doneTasksList) {
        this.project = project;
        try {
            project.setLabels(LabelApiClient.getInstance().getLabelsByProjectId(project.getId()));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        if (todoTasksList == null || inProgressTasksList == null || doneTasksList == null) {
            throw new RuntimeException("You must set the todoTasksList, inProgressTasksList and doneTasksList before setting the project");
        }

        addTaskToTasksList(todoTasksList, inProgressTasksList, doneTasksList);

    }

    public Project getProject() {
        return project;
    }

    public void setTodoTasksList(VBox todoTasksList) {
        this.todoTasksList = todoTasksList;
    }

    public void setInProgressTasksList(VBox inProgressTasksList) {
        this.inProgressTasksList = inProgressTasksList;
    }

    public void setDoneTasksList(VBox doneTasksList) {
        this.doneTasksList = doneTasksList;
    }

    public void resetController() throws JSONException {
        TabService.resetTab(todoTasksList, inProgressTasksList, doneTasksList);
        LabelApiClient.getInstance().getLabels(project).clear();
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void refreshTasks()  {
        TabService.resetTab(todoTasksList, inProgressTasksList, doneTasksList);
        addTaskToTasksList(todoTasksList, inProgressTasksList, doneTasksList);
    }

    private void addTaskToTasksList(VBox todoTasksList, VBox inProgressTasksList, VBox doneTasksList) {
        if (Project.getTasks() == null) return;

        Project.getTasks().forEach(task -> {
            createTask(task, task.getTitle());
            switch (task.getStatus()) {
                case "TODO" -> todoTasksList.getChildren().add(task.getTaskBox());
                case "IN PROGRESS" -> inProgressTasksList.getChildren().add(task.getTaskBox());
                case "DONE" -> doneTasksList.getChildren().add(task.getTaskBox());
            }
        });
    }

    public List<User> getAllUsers() {
        try {
            return  RoleApiClient.getInstance().getUsersByProject(project.getId());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
}

