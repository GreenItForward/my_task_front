package com.mytask.front.controller;

import com.mytask.front.model.LabelModel;
import com.mytask.front.model.Task;
import com.mytask.front.service.AppService;
import com.mytask.front.service.api.impl.TaskApiClient;
import com.mytask.front.service.view.PopupService;
import com.mytask.front.service.view.ProjectTabService;
import com.mytask.front.service.view.TabService;
import com.mytask.front.utils.enums.EString;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.List;

import static com.mytask.front.utils.enums.EString.CHANGE_ASSIGNED_LABELS;
import static com.mytask.front.utils.enums.EString.CHANGE_ASSIGNED_MEMBERS;

public class TaskDetailsController {
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea detailsTextArea;

    @FXML
    private DatePicker deadlineDatePicker;

    @FXML
    private Button changeAssignedMembersBtn, changeAssignedLabelsBtn, deleteTaskBtn;

    @FXML
    private ScrollPane labelsScrollPane;

    private Task task;

    private static TaskDetailsController instance;

    public TaskDetailsController() {

    }

    public static TaskDetailsController getInstance() {
        if (instance == null) {
            instance = new TaskDetailsController();
        }
        return instance;
    }


    @FXML
    private void initialize() {
        initData();
        setTextForUIElements();
        configureButtons();
        initializeListeners();
    }

    private void initData() {
        task = null;
    }

    private void configureButtons() {
        changeAssignedMembersBtn.setOnAction(event -> TabService.showAssignedMembers((Stage) changeAssignedMembersBtn.getScene().getWindow(), TaskApiClient.getInstance().getTaskById(task.getId())));
        changeAssignedLabelsBtn.setOnAction(event -> TabService.showLabels((Stage) changeAssignedLabelsBtn.getScene().getWindow(), task));
        deleteTaskBtn.setOnAction(event -> {
            if (task == null) {
                System.err.println("Cannot delete task because task is null.");
                return;
            }
            AppService.getTaskApiClient().deleteTask(task);
            ShowTabController.getInstance().getProject().deleteTask(task);
            ShowTabController.getInstance().refreshTasks();
            ProjectTabService.getInstance().closeCurrentPopup(deleteTaskBtn.getScene().getWindow());
        });
    }

    private void setTextForUIElements() {
        changeAssignedMembersBtn.setText(CHANGE_ASSIGNED_MEMBERS.toString());
        changeAssignedLabelsBtn.setText(CHANGE_ASSIGNED_LABELS.toString());
        deleteTaskBtn.setText(EString.DELETE_TASK.toString());
    }

    public void setTaskAndUpdateUI(Task task) {
        setTask(task);
        displayLabels();
        updateFields();
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @FXML
    public Task getTask() {
        return task;
    }

    private void updateFields() {
        if (task == null) {
            System.err.println("Cannot update task because task is null.");
            return;
        }

        titleTextField.setText(task.getTitle());
        detailsTextArea.setText(task.getDetails());
        deadlineDatePicker.setValue(task.getDeadline());
    }

    private void displayLabels() {
        Task task = getTask();
        if (task == null) {
            throw new IllegalArgumentException("Cannot display labels because task is null.");
        }

        List<LabelModel> labels = task.getLabels();

        VBox labelsContainer = new VBox(10);
        labelsContainer.setPadding(new Insets(10, 10, 10, 10));
        labelsContainer.setStyle("-fx-background-color: transparent;");

        for (LabelModel labelModel : labels) {
            Label label = new Label(labelModel.getNom());
            label.setTextFill(Color.WHITE);
            String color = AppService.colorToHexString(labelModel.getCouleur());
            label.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 5px; -fx-padding: 4; -fx-text-fill: black;");

            labelsContainer.getChildren().add(label);
        }

        labelsScrollPane.setContent(labelsContainer);
    }

    private void initializeListeners() {
        this.titleTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (task != null) {
                task.setTitle(newValue);
            }
        });

        this.detailsTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (task != null) {
                task.setDetails(newValue);
            }
        });

        deadlineDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (task != null) {
                task.getdeadlineDatePicker().setValue(newValue);
            }
        });
    }


}
