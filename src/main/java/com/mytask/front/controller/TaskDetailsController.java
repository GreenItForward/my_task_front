package com.mytask.front.controller;

import com.mytask.front.model.Task;
import com.mytask.front.service.api.impl.TaskApiClient;
import com.mytask.front.service.view.TabService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

import static com.mytask.front.utils.EString.CHANGE_ASSIGNED_LABELS;
import static com.mytask.front.utils.EString.CHANGE_ASSIGNED_MEMBERS;

public class TaskDetailsController {
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea detailsTextArea;

    @FXML
    private DatePicker deadlineDatePicker;

    @FXML
    private Button changeAssignedMembersBtn, changeAssignedLabelsBtn;

    private Task task;

    private TaskApiClient taskApiClient;

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
        task = null;
        taskApiClient = TaskApiClient.getInstance();

        changeAssignedMembersBtn.setText(CHANGE_ASSIGNED_MEMBERS.toString());
        changeAssignedLabelsBtn.setText(CHANGE_ASSIGNED_LABELS.toString());
        this.initializeListeners();
        changeAssignedMembersBtn.setOnAction(event -> TabService.showMembers((Stage) changeAssignedMembersBtn.getScene().getWindow()));
        changeAssignedLabelsBtn.setOnAction(event -> TabService.showLabels((Stage) changeAssignedLabelsBtn.getScene().getWindow(), task));
    }

    @FXML
    public void setTask(Task task) {
        this.task = Objects.requireNonNullElseGet(task, Task::new);
        updateFields();
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
/* TODO: Fix this method*/
/*
    public void saveTask() {
        if (task != null) {
            taskApiClient.saveTask(task);
        }
    }
 */

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
