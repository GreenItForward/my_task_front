package com.mytask.front.model;

import com.mytask.front.exception.AuthException;
import com.mytask.front.service.api.impl.AuthApiClient;
import com.mytask.front.utils.enums.EStatus;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Task {
    private int id;
    private StringProperty title;

    private StringProperty details;

    private String status;

    private int projectID;

    private DatePicker deadlineDatePicker;
    private StringProperty assignedTo;

    private List<LabelModel> labels;

    private HBox taskBox;
    private HBox labelBox;
    private TextField assignedToField;


    public Task() {
        this(null, null, null, null, null);
    }

    public Task(String title, LocalDate deadline, String assignedTo, DatePicker datePicker, String details) {
        this.id = 0;
        this.title = new SimpleStringProperty(title);
        this.details = new SimpleStringProperty(details);
        this.status = EStatus.TODO.getValue();
        this.projectID = 0;
        this.assignedTo = new SimpleStringProperty(assignedTo);
        this.deadlineDatePicker = new DatePicker(deadline);
        this.labels = new ArrayList<>();
    }

    public Task(int id, String titre, String description, String status, int userId, int projectId) throws AuthException {
        this.id = id;
        this.title = new SimpleStringProperty(titre);
        this.details = new SimpleStringProperty(description);
        this.status = status;
        this.projectID = projectId;
        this.assignedTo = new SimpleStringProperty(String.valueOf(AuthApiClient.getInstance().getUserById(userId).getPrenom()));
        this.deadlineDatePicker = new DatePicker();
        this.labels = new ArrayList<>();
    }

    public Task(int id, String titre, String description, String status, int projectId) {
        this.id = id;
        this.title = new SimpleStringProperty(titre);
        this.details = new SimpleStringProperty(description);
        this.status = status;
        this.projectID = projectId;
        this.deadlineDatePicker = new DatePicker();
        this.labels = new ArrayList<>();
        this.assignedTo = new SimpleStringProperty("");
    }

    public Task(int id, String titre, String description, EStatus status, int projectId) {
        this.id = id;
        this.title = new SimpleStringProperty(titre);
        this.details = new SimpleStringProperty(description);
        this.status = status.getValue();
        this.projectID = projectId;
        this.assignedTo = new SimpleStringProperty("");
        this.deadlineDatePicker = new DatePicker();
        this.labels = new ArrayList<>();
    }

    public Task(int id, String titre, String description, EStatus status, Integer userId, int projectId) {
        this.id = id;
        this.title = new SimpleStringProperty(titre);
        this.details = new SimpleStringProperty(description);
        this.status = status.getValue();
        this.projectID = projectId;
        this.assignedTo = new SimpleStringProperty(String.valueOf(userId));
        this.deadlineDatePicker = new DatePicker();
        this.labels = new ArrayList<>();
    }

    public Task(int id, String titre, String description, EStatus status, String deadline, int projectId) {
        this.id = id;
        this.title = new SimpleStringProperty(titre);
        this.details = new SimpleStringProperty(description);
        this.status = status.getValue();
        this.projectID = projectId;
        this.assignedTo = new SimpleStringProperty("");
        this.deadlineDatePicker = new DatePicker();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        if (deadline != null && !deadline.equals("null")) {
            this.deadlineDatePicker.setValue(LocalDate.from(LocalDateTime.parse(deadline, formatter)));
        }
        this.labels = new ArrayList<>();
    }

    public Task(int id, String titre, String description, EStatus status, String deadline, Integer userId, int projectId) {
        this.id = id;
        this.title = new SimpleStringProperty(titre);
        this.details = new SimpleStringProperty(description);
        this.status = status.getValue();
        this.projectID = projectId;
        this.assignedTo = new SimpleStringProperty(String.valueOf(userId));
        this.deadlineDatePicker = new DatePicker();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        if (deadline != null && !deadline.equals("null")) {
            this.deadlineDatePicker.setValue(LocalDate.from(LocalDateTime.parse(deadline, formatter)));
        }
        this.labels = new ArrayList<>();
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

    public User getAssignedTo(Integer userId) {
        if (assignedTo.get() == null || assignedTo.get().equals("")) {
            return new User();
        }

        try {
            Integer id = Objects.requireNonNullElseGet(userId, () -> {
                if (assignedTo.get().matches("\\d+")) {
                    return Integer.parseInt(assignedTo.get());
                } else {
                    throw new RuntimeException("assignedTo is not a number: " + assignedTo.get());
                }
            });
            if(id == 0) {
                return new User();
            }

            return AuthApiClient.getInstance().getUserById(id);
        } catch (AuthException e) {
            throw new RuntimeException("GetAssignedTo failed: "+e);
        }
    }


    public User getAssignedTo() {
        return getAssignedTo(null);
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

    public String getStatus() {
        if (status == null) {
            return "";
        }

        return status;
    }

    public void setStatus(String status) {
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

    public String toJSON(Integer userId) {
        if (userId != null) {
            this.assignedTo = new SimpleStringProperty(String.valueOf(userId));
        }

        if (userId == null) {
            userId = getAssignedTo().getId();
        }

        String details = this.getDetails().replaceAll("\\r?\\n", "\\\\n");
        return "{" +
                "\"id\":" + id +
                ", \"title\":\"" + title.getValue() + '\"' +
                ", \"description\":\"" + details + '\"' +
                ", \"status\":\"" + status + '\"' +
                ", \"userID\":" + userId +
                ", \"deadline\":\"" + deadlineDatePicker.getValue() + '\"' +
                ", \"projectID\":" + projectID +
                '}';
    }

    public String toJSON() {
        return toJSON(null);
    }

    public String createTaskJson() {
        return "{" +
                "\"title\":\"" + title.getValue() + '\"' +
                ", \"description\":\"" + this.getDetails() + '\"' +
                ", \"status\":\"" + status + '\"' +
                ", \"projectID\":" + projectID +
                '}';
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title=" + title +
                ", details=" + details +
                ", status='" + status + '\'' +
                ", projectID=" + projectID +
                ", deadlineDatePicker=" + deadlineDatePicker +
                ", assignedTo=" + getAssignedTo().getPrenom() +
                ", labels=" + labels +
                ", taskBox=" + taskBox +
                ", labelBox=" + labelBox +
                '}';
    }

}




