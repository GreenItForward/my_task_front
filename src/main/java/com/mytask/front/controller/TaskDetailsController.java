package com.mytask.front.controller;
import com.mytask.front.utils.EPage;
import com.mytask.front.service.view.ScreenService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TaskDetailsController {
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea detailsTextArea;

    @FXML
    private DatePicker deadlineDatePicker;

    private ScreenService screenService;
    private Task task;


    public TaskDetailsController() {
    }

    @FXML
    private void initialize() {
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
        this.task = task;
        updateFields();
    }

    private void updateFields() {
        if (task != null) {
            titleTextField.setText(task.getTitle());
            detailsTextArea.setText(task.getDetails());
            deadlineDatePicker.setValue(task.getDeadline());
        }
    }

}
