package com.mytask.front.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class Task {
    private StringProperty title;
    private DatePicker deadlineDatePicker;
    private StringProperty assignedTo;

    private StringProperty details;

    public Task(String title, LocalDate deadline, String assignedTo, DatePicker datePicker, String details) {
        this.title = new SimpleStringProperty(title);
        this.assignedTo = new SimpleStringProperty(assignedTo);
        this.deadlineDatePicker = new DatePicker(deadline);
        this.details = new SimpleStringProperty(details);
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

}



