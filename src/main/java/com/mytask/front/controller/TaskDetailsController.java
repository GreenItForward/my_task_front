package com.mytask.front.controller;
import com.mytask.front.model.Task;
import com.mytask.front.service.api.impl.TaskApiClient;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Objects;

public class TaskDetailsController {
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea detailsTextArea;

    @FXML
    private DatePicker deadlineDatePicker;

    private Task task;

    private TaskApiClient taskApiClient;

    private static TaskDetailsController instance;

    // Change the constructor from private to public
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
}
