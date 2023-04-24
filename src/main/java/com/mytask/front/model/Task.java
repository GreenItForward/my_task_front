package com.mytask.front.model;

import com.mytask.front.utils.EStatus;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Task {
    private int id;
    private StringProperty title;

    private StringProperty details;

    private EStatus status;

    private int projectID;

    private DatePicker deadlineDatePicker;
    private StringProperty assignedTo;

    private List<LabelModel> labels;

    private HBox taskBox;
    private HBox labelBox;

    public Task(String title, LocalDate deadline, String assignedTo, DatePicker datePicker, String details) {
        this.id = 14;
        this.title = new SimpleStringProperty(title);
        this.details = new SimpleStringProperty(details);
        this.status = EStatus.TODO;
        this.projectID = 38;
        this.assignedTo = new SimpleStringProperty(assignedTo);
        this.deadlineDatePicker = new DatePicker(deadline);
        this.labels = new ArrayList<>();
    }

    public Task() {
        this(null, null, null, null, null);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAssignedTo() {
        return assignedTo.get();
    }

    public StringProperty assignedToProperty() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo.set(assignedTo);
    }

    public DatePicker getdeadlineDatePicker() {
        return deadlineDatePicker;
    }

    public void setdeadlineDatePicker(DatePicker datePicker) {
        this.deadlineDatePicker = datePicker;
    }

    public String getDetails() {
        if (details.get() == null) {
            return "";
        }

        return details.get();
    }


    public void setDetails(String newValue) {
        this.details.set(newValue);
    }

    public LocalDate getDeadline() {
        return deadlineDatePicker.getValue();
    }

    public void setDeadline(LocalDate plusDays) {
        this.deadlineDatePicker.setValue(plusDays);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public List<LabelModel> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelModel> labels) {
        this.labels = labels;
    }

    public HBox getTaskBox() {
        return taskBox;
    }

    public void setTaskBox(HBox taskBox) {
        this.taskBox = taskBox;
    }

    public HBox getLabelBox() {
        return labelBox;
    }

    public void setLabelBox(HBox labelBox) {
        this.labelBox = labelBox;
    }

    public String parseDueDate(String dueDateText) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("d MMM", Locale.FRANCE);
        try {
            LocalDate date = LocalDate.parse(dueDateText, inputFormatter);
            return date.format(outputFormatter);
        } catch (DateTimeParseException e) {
            // Handle the error, e.g., show an error message to the user
            return null;
        }
    }

    public String toJSON() {
        return "{" +
                "\"id\":" + id +
                ", \"title\":\"" + title.getValue() + '\"' +
                ", \"description\":\"" + this.getDetails() + '\"' +
                ", \"status\":\"" + status + '\"' +
                ", \"deadline\":\"" + deadlineDatePicker.getValue() + '\"' +
                ", \"projectID\":" + projectID +
                //    ", \"assignedTo\":\"" + assignedTo + '\"' +
                '}';
    }
}



